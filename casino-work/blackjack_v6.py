"""
Hazy Casino - Blackjack Table v6
D-shaped table with:
- Green felt top
- Dark wood rim + pedestal base
- Colored chip stacks (red, green, yellow/gold)
- Playing cards (white face, blue back)
- Beer glasses (amber + foam)
- Martini glass
- Wine bottle
- Player seat positions
317 format .dat model
"""

import struct
import math
import os

class ModelBuilder:
    def __init__(self):
        self.vertices = []
        self.faces = []
    
    def add_vertex(self, x, y, z):
        idx = len(self.vertices)
        self.vertices.append((int(x), int(-y), int(z)))  # y inverted in RS
        return idx
    
    def add_face(self, v_indices, color_rgb, alpha=255, shade=True):
        """Add face with RS HSL color"""
        hsl = self.rgb_to_rs_hsl(color_rgb[0], color_rgb[1], color_rgb[2])
        self.faces.append({
            'vertices': v_indices,
            'color': hsl,
            'alpha': alpha,
            'shade': shade,
        })
    
    def rgb_to_rs_hsl(self, r, g, b):
        r2, g2, b2 = r/255.0, g/255.0, b/255.0
        mx, mn = max(r2,g2,b2), min(r2,g2,b2)
        l = (mx+mn)/2.0
        if mx == mn:
            h = s = 0.0
        else:
            d = mx - mn
            s = d/(2.0-mx-mn) if l > 0.5 else d/(mx+mn)
            if mx == r2: h = (g2-b2)/d + (6 if g2<b2 else 0)
            elif mx == g2: h = (b2-r2)/d + 2
            else: h = (r2-g2)/d + 4
            h /= 6.0
        rh = int(h * 63) & 0x3F
        rs = int(s * 7) & 0x7
        rl = int(l * 127) & 0x7F
        return (rh << 10) | (rs << 7) | rl
    
    # === SHAPE PRIMITIVES ===
    
    def add_box(self, cx, cy, cz, sx, sy, sz, color):
        """Box centered at cx,cy,cz with size sx,sy,sz"""
        hx, hy, hz = sx/2, sy/2, sz/2
        v = [
            self.add_vertex(cx-hx, cy-hy, cz-hz),  # 0 bottom-left-back
            self.add_vertex(cx+hx, cy-hy, cz-hz),  # 1 bottom-right-back
            self.add_vertex(cx+hx, cy-hy, cz+hz),  # 2 bottom-right-front
            self.add_vertex(cx-hx, cy-hy, cz+hz),  # 3 bottom-left-front
            self.add_vertex(cx-hx, cy+hy, cz-hz),  # 4 top-left-back
            self.add_vertex(cx+hx, cy+hy, cz-hz),  # 5 top-right-back
            self.add_vertex(cx+hx, cy+hy, cz+hz),  # 6 top-right-front
            self.add_vertex(cx-hx, cy+hy, cz+hz),  # 7 top-left-front
        ]
        self.add_face([v[4],v[5],v[6]], color)  # top1
        self.add_face([v[4],v[6],v[7]], color)  # top2
        self.add_face([v[0],v[3],v[2]], color)  # bottom1
        self.add_face([v[0],v[2],v[1]], color)  # bottom2
        self.add_face([v[0],v[1],v[5]], color)  # back1
        self.add_face([v[0],v[5],v[4]], color)  # back2
        self.add_face([v[2],v[3],v[7]], color)  # front1
        self.add_face([v[2],v[7],v[6]], color)  # front2
        self.add_face([v[0],v[4],v[7]], color)  # left1
        self.add_face([v[0],v[7],v[3]], color)  # left2
        self.add_face([v[1],v[2],v[6]], color)  # right1
        self.add_face([v[1],v[6],v[5]], color)  # right2
    
    def add_cylinder(self, cx, cy, cz, radius, height, segments, color, top=True, bottom=True):
        """Vertical cylinder"""
        verts_bottom = []
        verts_top = []
        for i in range(segments):
            angle = 2 * math.pi * i / segments
            px = cx + radius * math.cos(angle)
            pz = cz + radius * math.sin(angle)
            verts_bottom.append(self.add_vertex(px, cy, pz))
            verts_top.append(self.add_vertex(px, cy + height, pz))
        
        center_bottom = self.add_vertex(cx, cy, cz)
        center_top = self.add_vertex(cx, cy + height, cz)
        
        for i in range(segments):
            ni = (i + 1) % segments
            # Side
            self.add_face([verts_bottom[i], verts_top[i], verts_top[ni]], color)
            self.add_face([verts_bottom[i], verts_top[ni], verts_bottom[ni]], color)
            # Top
            if top:
                self.add_face([center_top, verts_top[i], verts_top[ni]], color)
            # Bottom
            if bottom:
                self.add_face([center_bottom, verts_bottom[ni], verts_bottom[i]], color)
    
    def add_d_shape(self, cx, cy, cz, width, depth, height, segments, color_top, color_side):
        """D-shaped table top - flat back, curved front"""
        hw = width / 2
        # Back edge vertices (straight)
        back_bottom_l = self.add_vertex(cx - hw, cy, cz - depth/3)
        back_bottom_r = self.add_vertex(cx + hw, cy, cz - depth/3)
        back_top_l = self.add_vertex(cx - hw, cy + height, cz - depth/3)
        back_top_r = self.add_vertex(cx + hw, cy + height, cz - depth/3)
        
        # Curved front
        curve_bottom = []
        curve_top = []
        for i in range(segments + 1):
            angle = -math.pi/2 + math.pi * i / segments
            px = cx + hw * math.cos(angle)
            pz = cz + depth * 0.55 * (1 + math.sin(angle))
            curve_bottom.append(self.add_vertex(px, cy, pz))
            curve_top.append(self.add_vertex(px, cy + height, pz))
        
        # Top surface triangles
        center_top = self.add_vertex(cx, cy + height, cz + depth/4)
        # Back top
        self.add_face([back_top_l, back_top_r, center_top], color_top)
        # Fan from center to curve
        for i in range(segments):
            self.add_face([center_top, curve_top[i], curve_top[i+1]], color_top)
        # Connect back corners to curve ends
        self.add_face([back_top_l, center_top, curve_top[0]], color_top)
        self.add_face([center_top, back_top_r, curve_top[segments]], color_top)
        
        # Side panels - back
        self.add_face([back_bottom_l, back_bottom_r, back_top_r], color_side)
        self.add_face([back_bottom_l, back_top_r, back_top_l], color_side)
        
        # Side panels - curve
        for i in range(segments):
            self.add_face([curve_bottom[i], curve_bottom[i+1], curve_top[i+1]], color_side)
            self.add_face([curve_bottom[i], curve_top[i+1], curve_top[i]], color_side)
        
        # Connect back to curve sides
        self.add_face([back_bottom_l, curve_bottom[0], curve_top[0]], color_side)
        self.add_face([back_bottom_l, curve_top[0], back_top_l], color_side)
        self.add_face([curve_bottom[segments], back_bottom_r, back_top_r], color_side)
        self.add_face([curve_bottom[segments], back_top_r, curve_top[segments]], color_side)
        
        # Bottom
        center_bottom = self.add_vertex(cx, cy, cz + depth/4)
        self.add_face([back_bottom_r, back_bottom_l, center_bottom], color_side)
        for i in range(segments):
            self.add_face([center_bottom, curve_bottom[i+1], curve_bottom[i]], color_side)
        self.add_face([center_bottom, back_bottom_l, curve_bottom[0]], color_side)
        self.add_face([back_bottom_r, center_bottom, curve_bottom[segments]], color_side)
    
    def add_chip_stack(self, cx, cy, cz, color, count=5):
        """Stack of poker chips"""
        chip_h = 3
        chip_r = 6
        for i in range(count):
            self.add_cylinder(cx, cy + i * chip_h, cz, chip_r, chip_h, 8, color)
    
    def add_card(self, cx, cy, cz, face_up=True, rotation=0):
        """Playing card - thin rectangle"""
        w, d = 10, 14
        h = 1
        # Card body
        white = (255, 255, 255)
        blue = (20, 40, 140)
        color = white if face_up else blue
        
        hw, hd = w/2, d/2
        cos_r = math.cos(math.radians(rotation))
        sin_r = math.sin(math.radians(rotation))
        
        corners = [(-hw, -hd), (hw, -hd), (hw, hd), (-hw, hd)]
        rotated = [(cx + c[0]*cos_r - c[1]*sin_r, cz + c[0]*sin_r + c[1]*cos_r) for c in corners]
        
        top_verts = [self.add_vertex(r[0], cy + h, r[1]) for r in rotated]
        bot_verts = [self.add_vertex(r[0], cy, r[1]) for r in rotated]
        
        self.add_face([top_verts[0], top_verts[1], top_verts[2]], color)
        self.add_face([top_verts[0], top_verts[2], top_verts[3]], color)
        self.add_face([bot_verts[2], bot_verts[1], bot_verts[0]], blue)
        self.add_face([bot_verts[3], bot_verts[2], bot_verts[0]], blue)
    
    def add_beer_glass(self, cx, cy, cz):
        """Beer glass - cylinder with amber fill + foam top"""
        # Glass body (slight transparency feel - use lighter color)
        glass_color = (220, 210, 170)
        self.add_cylinder(cx, cy, cz, 5, 20, 8, glass_color)
        # Beer (amber)
        beer_color = (210, 160, 40)
        self.add_cylinder(cx, cy + 2, cz, 4.5, 14, 8, beer_color, top=False)
        # Foam
        foam_color = (255, 250, 230)
        self.add_cylinder(cx, cy + 16, cz, 5, 5, 8, foam_color)
    
    def add_martini_glass(self, cx, cy, cz):
        """Martini glass - stem + triangular bowl"""
        silver = (200, 200, 210)
        # Base
        self.add_cylinder(cx, cy, cz, 6, 2, 8, silver)
        # Stem
        self.add_cylinder(cx, cy + 2, cz, 1.5, 14, 6, silver)
        # Bowl (cone - wider at top)
        segs = 8
        for i in range(segs):
            angle1 = 2 * math.pi * i / segs
            angle2 = 2 * math.pi * (i+1) / segs
            # Bottom of bowl (narrow)
            b1 = self.add_vertex(cx + 2*math.cos(angle1), cy + 16, cz + 2*math.sin(angle1))
            b2 = self.add_vertex(cx + 2*math.cos(angle2), cy + 16, cz + 2*math.sin(angle2))
            # Top of bowl (wide)
            t1 = self.add_vertex(cx + 8*math.cos(angle1), cy + 24, cz + 8*math.sin(angle1))
            t2 = self.add_vertex(cx + 8*math.cos(angle2), cy + 24, cz + 8*math.sin(angle2))
            self.add_face([b1, t1, t2], silver)
            self.add_face([b1, t2, b2], silver)
        # Liquid (green - martini)
        liquid = (100, 200, 80)
        self.add_cylinder(cx, cy + 18, cz, 6, 4, 8, liquid, bottom=False)
    
    def add_bottle(self, cx, cy, cz):
        """Wine/liquor bottle"""
        dark_green = (10, 60, 20)
        # Body
        self.add_cylinder(cx, cy, cz, 5, 22, 8, dark_green)
        # Neck
        self.add_cylinder(cx, cy + 22, cz, 2.5, 12, 6, dark_green)
        # Cap
        gold = (200, 170, 50)
        self.add_cylinder(cx, cy + 34, cz, 3, 3, 6, gold)
    
    def build_blackjack_table(self):
        """Build the complete blackjack table"""
        # Colors
        dark_wood = (60, 35, 15)
        green_felt = (20, 100, 35)
        gold_trim = (180, 150, 40)
        
        # === TABLE BASE / PEDESTAL ===
        # Central thick pedestal
        self.add_box(0, 0, 10, 40, 8, 30, dark_wood)
        # Wider foot
        self.add_box(0, 0, 10, 55, 4, 40, dark_wood)
        # Gold trim on foot
        self.add_box(0, 4, 10, 57, 2, 42, gold_trim)
        
        # Upper support column
        self.add_box(0, 8, 10, 30, 30, 25, dark_wood)
        
        # === TABLE TOP ===
        # D-shaped felt surface
        self.add_d_shape(0, 38, -15, 130, 90, 6, 16, green_felt, dark_wood)
        
        # Gold rim on edge (slightly larger D)
        self.add_d_shape(0, 43, -15, 134, 93, 2, 16, gold_trim, gold_trim)
        
        # Raised back rail (dealer side)
        self.add_box(0, 44, -28, 120, 5, 8, dark_wood)
        self.add_box(0, 49, -28, 118, 2, 6, gold_trim)
        
        # === CHIP STACKS ===
        table_y = 45  # on top of table
        
        # Red chips (left area)
        self.add_chip_stack(-35, table_y, 5, (180, 30, 30), 6)
        self.add_chip_stack(-35, table_y, -8, (180, 30, 30), 4)
        
        # Green chips (center-left)  
        self.add_chip_stack(-20, table_y, 0, (30, 140, 40), 5)
        self.add_chip_stack(-20, table_y, -12, (30, 140, 40), 7)
        
        # Gold/yellow chips (center)
        self.add_chip_stack(-5, table_y, 5, (200, 170, 40), 4)
        self.add_chip_stack(-5, table_y, -8, (200, 170, 40), 3)
        
        # Blue chips (right)
        self.add_chip_stack(15, table_y, 0, (30, 50, 170), 5)
        self.add_chip_stack(15, table_y, -10, (30, 50, 170), 6)
        
        # More red chips (far right)
        self.add_chip_stack(30, table_y, 5, (180, 30, 30), 3)
        
        # === PLAYING CARDS ===
        card_y = 45
        
        # Dealer cards (back row)
        self.add_card(-30, card_y, -18, face_up=True, rotation=5)
        self.add_card(-10, card_y, -20, face_up=True, rotation=-3)
        self.add_card(10, card_y, -18, face_up=False, rotation=8)
        self.add_card(30, card_y, -20, face_up=True, rotation=-5)
        
        # Player cards (front, scattered)
        self.add_card(-40, card_y, 18, face_up=True, rotation=15)
        self.add_card(-42, card_y, 22, face_up=True, rotation=25)
        self.add_card(-15, card_y, 25, face_up=True, rotation=-10)
        self.add_card(-12, card_y, 29, face_up=True, rotation=5)
        self.add_card(12, card_y, 25, face_up=True, rotation=20)
        self.add_card(15, card_y, 29, face_up=False, rotation=10)
        self.add_card(38, card_y, 20, face_up=True, rotation=-15)
        self.add_card(40, card_y, 24, face_up=True, rotation=-8)
        
        # Deck (stacked cards - blue backs)
        for i in range(4):
            self.add_card(45, card_y + i*1.5, -18, face_up=False, rotation=0)
        
        # === BEER GLASSES ===
        glass_y = 45
        self.add_beer_glass(-55, glass_y, 15)
        self.add_beer_glass(55, glass_y, 15)
        self.add_beer_glass(-45, glass_y, 35)
        self.add_beer_glass(45, glass_y, 35)
        
        # === MARTINI GLASS ===
        self.add_martini_glass(0, glass_y, 30)
        
        # === BOTTLE ===
        self.add_bottle(-25, glass_y, -22)
    
    def encode_317(self):
        """Encode to 317 .dat model format"""
        num_verts = len(self.vertices)
        num_faces = len(self.faces)
        
        # Separate face data
        face_colors = []
        face_alphas = []
        face_types = []
        face_a = []
        face_b = []
        face_c = []
        
        has_alpha = False
        
        for f in self.faces:
            verts = f['vertices']
            face_a.append(verts[0])
            face_b.append(verts[1])
            face_c.append(verts[2])
            face_colors.append(f['color'])
            face_alphas.append(f['alpha'])
            face_types.append(0)  # flat shading
            if f['alpha'] != 255:
                has_alpha = True
        
        buf = bytearray()
        
        # Vertex positions (delta encoded)
        # X deltas
        x_vals = [v[0] for v in self.vertices]
        y_vals = [v[1] for v in self.vertices]
        z_vals = [v[2] for v in self.vertices]
        
        # Calculate offsets for the data sections
        # We need to compute sizes first
        
        # Encode vertex deltas
        def encode_delta(val):
            if val == 0:
                return b''  # flag = 0
            elif -64 <= val < 64:
                return struct.pack('>b', val) if val >= 0 else struct.pack('>b', val & 0xFF)
            else:
                return struct.pack('>h', val)
        
        def delta_flag(val):
            if val == 0:
                return 0
            elif -64 <= val < 64:
                return 1  # smart byte
            else:
                return 2  # smart short
        
        # Build the data arrays
        x_data = bytearray()
        y_data = bytearray()
        z_data = bytearray()
        x_flags = []
        y_flags = []
        z_flags = []
        
        for i in range(num_verts):
            dx = x_vals[i] - (x_vals[i-1] if i > 0 else 0)
            dy = y_vals[i] - (y_vals[i-1] if i > 0 else 0)
            dz = z_vals[i] - (z_vals[i-1] if i > 0 else 0)
            
            xf = delta_flag(dx)
            yf = delta_flag(dy)
            zf = delta_flag(dz)
            x_flags.append(xf)
            y_flags.append(yf)
            z_flags.append(zf)
            
            if xf == 1:
                x_data.extend(struct.pack('>B', dx if dx >= 0 else dx + 128))
            elif xf == 2:
                # Encode as signed magnitude: bit 14 = sign, bits 0-13 = magnitude
                mag = abs(dx)
                val = mag
                if dx < 0:
                    val |= 0x4000
                x_data.extend(struct.pack('>H', val))
            
            if yf == 1:
                y_data.extend(struct.pack('>B', dy if dy >= 0 else dy + 128))
            elif yf == 2:
                mag = abs(dy)
                val = mag
                if dy < 0:
                    val |= 0x4000
                y_data.extend(struct.pack('>H', val))
            
            if zf == 1:
                z_data.extend(struct.pack('>B', dz if dz >= 0 else dz + 128))
            elif zf == 2:
                mag = abs(dz)
                val = mag
                if dz < 0:
                    val |= 0x4000
                z_data.extend(struct.pack('>H', val))
        
        # Face vertex indices (delta encoded)
        face_idx_data = bytearray()
        face_type_data = bytearray()
        
        for i in range(num_faces):
            a, b, c = face_a[i], face_b[i], face_c[i]
            face_type_data.append(0)  # type indicator
            
            # Type 0: a = smart, b = a - smart, c = b - smart
            # But 317 uses specific encoding...
            # Let's use the simplest approach
        
        # Actually, let's use a simpler 317 format encoder
        # that matches what the client expects
        
        return self._encode_317_simple()
    
    def _encode_317_simple(self):
        """Simple 317 model encoder"""
        num_verts = len(self.vertices)
        num_faces = len(self.faces)
        
        # Extract face data
        face_a_arr = []
        face_b_arr = []
        face_c_arr = []
        face_colors_arr = []
        face_render_type = []
        
        for f in self.faces:
            v = f['vertices']
            face_a_arr.append(v[0])
            face_b_arr.append(v[1])
            face_c_arr.append(v[2])
            face_colors_arr.append(f['color'])
            face_render_type.append(0)
        
        # Vertex coordinate deltas
        x_sorted = sorted(range(num_verts), key=lambda i: self.vertices[i][0])
        y_sorted = sorted(range(num_verts), key=lambda i: self.vertices[i][1])
        z_sorted = sorted(range(num_verts), key=lambda i: self.vertices[i][2])
        
        # Encode vertex flags and data
        vertex_flags = bytearray()
        x_data = bytearray()
        y_data = bytearray()
        z_data = bytearray()
        
        prev_x = prev_y = prev_z = 0
        for i in range(num_verts):
            vx, vy, vz = self.vertices[i]
            dx = vx - prev_x
            dy = vy - prev_y
            dz = vz - prev_z
            
            flag = 0
            if dx != 0:
                flag |= 1
            if dy != 0:
                flag |= 2
            if dz != 0:
                flag |= 4
            vertex_flags.append(flag)
            prev_x, prev_y, prev_z = vx, vy, vz
        
        # Now encode actual coordinate values with smart encoding
        for i in range(num_verts):
            vx = self.vertices[i][0]
            dx = vx - (self.vertices[i-1][0] if i > 0 else 0)
            if dx != 0:
                self._write_signed_smart(x_data, dx)
        
        for i in range(num_verts):
            vy = self.vertices[i][1]
            dy = vy - (self.vertices[i-1][1] if i > 0 else 0)
            if dy != 0:
                self._write_signed_smart(y_data, dy)
        
        for i in range(num_verts):
            vz = self.vertices[i][2]
            dz = vz - (self.vertices[i-1][2] if i > 0 else 0)
            if dz != 0:
                self._write_signed_smart(z_data, dz)
        
        # Face indices - type encoding
        face_idx_types = bytearray()
        face_idx_data = bytearray()
        
        for i in range(num_faces):
            a, b, c = face_a_arr[i], face_b_arr[i], face_c_arr[i]
            face_idx_types.append(1)
            self._write_usmart(face_idx_data, a)
            self._write_usmart(face_idx_data, b)
            self._write_usmart(face_idx_data, c)
        
        # Face colors
        face_color_data = bytearray()
        prev_color = 0
        for i in range(num_faces):
            delta = face_colors_arr[i] - prev_color
            self._write_signed_smart(face_color_data, delta)
            prev_color = face_colors_arr[i]
        
        # Now assemble the 317 model
        # Header structure:
        # numVertices (ushort)
        # numFaces (ushort) 
        # numTextureTriangles (ubyte)
        # useTextures (ubyte) = 0
        # useFacePriority (ubyte) = 0 (all same priority)
        # useTransparency (ubyte) = 0
        # useAnimayaGroups (ubyte) = 0  
        # useLabelGroups (ubyte) = 0
        # Then data section sizes, then data
        
        # Compute section sizes
        vertex_flags_size = len(vertex_flags)
        face_idx_types_size = len(face_idx_types)
        face_priority_size = 0  # all same
        face_color_size = len(face_color_data)
        x_data_size = len(x_data)
        y_data_size = len(y_data)
        z_data_size = len(z_data)
        face_idx_data_size = len(face_idx_data)
        
        # Build full buffer
        out = bytearray()
        
        # Header
        out.extend(struct.pack('>H', num_verts))
        out.extend(struct.pack('>H', num_faces))
        out.append(0)  # numTextureTriangles
        
        out.append(0)  # useTextures  
        out.append(0)  # useFacePriority (0 = single value follows)
        out.append(0)  # useTransparency
        out.append(0)  # useAnimayaGroups
        out.append(0)  # useLabelGroups
        
        # Data section sizes
        out.extend(struct.pack('>H', x_data_size))
        out.extend(struct.pack('>H', y_data_size))
        out.extend(struct.pack('>H', z_data_size))
        out.extend(struct.pack('>H', face_idx_data_size))
        
        # Vertex flags (directional flags)
        out.extend(vertex_flags)
        
        # Face types (index encoding types)
        out.extend(face_idx_types)
        
        # Face priority (single byte since useFacePriority=0)
        out.append(10)  # priority value
        
        # Face colors
        out.extend(face_color_data)
        
        # Vertex data
        out.extend(x_data)
        out.extend(y_data)
        out.extend(z_data)
        
        # Face index data
        out.extend(face_idx_data)
        
        return bytes(out)
    
    def _write_usmart(self, buf, val):
        """Unsigned smart"""
        if val < 128:
            buf.append(val)
        else:
            buf.extend(struct.pack('>H', val + 32768))
    
    def _write_signed_smart(self, buf, val):
        """Signed smart"""
        if -64 <= val < 64:
            buf.append((val + 64) & 0xFF)
        else:
            buf.extend(struct.pack('>H', (val + 16384) & 0xFFFF))


def main():
    builder = ModelBuilder()
    builder.build_blackjack_table()
    
    print(f"Vertices: {len(builder.vertices)}")
    print(f"Faces: {len(builder.faces)}")
    
    data = builder.encode_317()
    
    out_path = r"C:\hazy\custom-models\models\blackjack.dat"
    os.makedirs(os.path.dirname(out_path), exist_ok=True)
    
    with open(out_path, 'wb') as f:
        f.write(data)
    
    print(f"Model saved to {out_path} ({len(data)} bytes)")
    print(f"\nNext: run HazyPacker to pack into cache")


if __name__ == '__main__':
    main()
