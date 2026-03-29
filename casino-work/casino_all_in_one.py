"""
Hazy Casino Cleanup - All in One
1) Extracts file 945 (locations) from cache index 4
2) Parses & analyzes all objects
3) Removes walls/blockers + mahogany tables
4) Saves cleaned locations.dat ready for HazyPacker
"""

import struct, os, sys
from itertools import groupby

CACHE_DIR = r"C:\hazy\hazy-swift\cache"
WORK_DIR = r"C:\hazy\casino-work"
FILE_ID = 945  # locations for region 8755
INDEX_ID = 4

# === STEP 1: Extract from cache ===

def extract_file(cache_dir, index_id, file_id):
    """Extract a file from 317 cache (idx + dat2)"""
    idx_path = os.path.join(cache_dir, f"main_file_cache.idx{index_id}")
    dat_path = os.path.join(cache_dir, "main_file_cache.dat2")
    
    with open(idx_path, 'rb') as f:
        f.seek(file_id * 6)
        entry = f.read(6)
    
    if len(entry) < 6:
        raise Exception(f"File {file_id} not found in idx{index_id}")
    
    size = (entry[0] << 16) | (entry[1] << 8) | entry[2]
    sector = (entry[3] << 16) | (entry[4] << 8) | entry[5]
    
    if size == 0 or sector == 0:
        raise Exception(f"File {file_id} is empty (size={size}, sector={sector})")
    
    print(f"  idx entry: size={size}, start_sector={sector}")
    
    data = bytearray()
    remaining = size
    chunk = 0
    
    with open(dat_path, 'rb') as f:
        while remaining > 0:
            f.seek(sector * 520)
            header = f.read(8)
            
            h_file_id = (header[0] << 8) | header[1]
            h_chunk = (header[2] << 8) | header[3]
            h_next_sector = (header[4] << 16) | (header[5] << 8) | header[6]
            h_index_id = header[7]
            
            read_size = min(remaining, 512)
            block = f.read(read_size)
            data.extend(block)
            
            remaining -= read_size
            sector = h_next_sector
            chunk += 1
            
            if remaining > 0 and sector == 0:
                raise Exception(f"Unexpected end of data chain at chunk {chunk}")
    
    return bytes(data)


# === STEP 2: Parse locations.dat ===

OBJECT_TYPES = {
    0: "Wall (straight)", 1: "Wall (diag corner)", 2: "Wall (corner)",
    3: "Wall (sq corner)", 4: "Wall deco (straight)", 5: "Wall deco (diag offset)",
    6: "Wall deco (diag)", 7: "Wall deco (diag dbl)", 8: "Wall deco (diag corner)",
    9: "Diagonal wall", 10: "Ground object", 11: "Ground object (diag)",
    12: "Roof (straight)", 13: "Roof (diag offset)", 14: "Roof (diag)",
    15: "Roof (corner concave)", 16: "Roof (corner convex)", 17: "Roof (flat)",
    18: "Roof edge (straight)", 19: "Roof edge (diag offset)", 20: "Roof edge (diag)",
    21: "Roof edge (corner)", 22: "Floor decal",
}

WALL_TYPES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
MAHOGANY_IDS = {6332, 6333, 8115, 13295}


def read_smart(data, pos):
    if data[pos] < 128:
        return data[pos], pos + 1
    val = struct.unpack_from('>H', data, pos)[0] - 32768
    return val, pos + 2


def parse_locations(data):
    objects = []
    pos = 0
    object_id = -1
    
    while pos < len(data):
        id_offset, pos = read_smart(data, pos)
        if id_offset == 0:
            break
        object_id += id_offset
        
        location = 0
        while pos < len(data):
            loc_offset, pos = read_smart(data, pos)
            if loc_offset == 0:
                break
            location += loc_offset - 1
            
            local_y = location & 0x3F
            local_x = (location >> 6) & 0x3F
            plane = location >> 12
            
            attributes = data[pos]; pos += 1
            obj_type = attributes >> 2
            rotation = attributes & 3
            
            objects.append({
                'id': object_id, 'x': local_x, 'y': local_y,
                'plane': plane, 'type': obj_type, 'rotation': rotation,
                'type_name': OBJECT_TYPES.get(obj_type, f"Unknown({obj_type})"),
            })
    return objects


# === STEP 3: Encode back ===

def write_smart(buf, value):
    if value < 128:
        buf.append(value)
    else:
        val = value + 32768
        buf.append((val >> 8) & 0xFF)
        buf.append(val & 0xFF)


def encode_locations(objects):
    if not objects:
        buf = bytearray()
        write_smart(buf, 0)
        return bytes(buf)
    
    objects.sort(key=lambda o: (o['id'], (o['plane'] << 12) | (o['x'] << 6) | o['y']))
    buf = bytearray()
    prev_id = -1
    
    for obj_id, group in groupby(objects, key=lambda o: o['id']):
        group_list = list(group)
        write_smart(buf, obj_id - prev_id)
        prev_id = obj_id
        
        prev_loc = 0
        for obj in group_list:
            packed_loc = (obj['plane'] << 12) | (obj['x'] << 6) | obj['y']
            write_smart(buf, packed_loc - prev_loc + 1)
            prev_loc = packed_loc
            buf.append((obj['type'] << 2) | (obj['rotation'] & 3))
        
        write_smart(buf, 0)
    
    write_smart(buf, 0)
    return bytes(buf)


# === MAIN ===

def main():
    os.makedirs(WORK_DIR, exist_ok=True)
    
    # --- Extract ---
    print("=" * 60)
    print(f"STEP 1: Extracting file {FILE_ID} from index {INDEX_ID}")
    print("=" * 60)
    
    raw_data = extract_file(CACHE_DIR, INDEX_ID, FILE_ID)
    
    orig_path = os.path.join(WORK_DIR, "locations_8755_original.dat")
    with open(orig_path, 'wb') as f:
        f.write(raw_data)
    print(f"  Saved original: {orig_path} ({len(raw_data)} bytes)")
    
    # --- Parse ---
    print(f"\n{'=' * 60}")
    print("STEP 2: Analyzing objects")
    print("=" * 60)
    
    objects = parse_locations(raw_data)
    print(f"  Total object placements: {len(objects)}")
    
    # Type summary
    type_counts = {}
    for o in objects:
        type_counts[o['type']] = type_counts.get(o['type'], 0) + 1
    
    print(f"\n  TYPE BREAKDOWN:")
    for t in sorted(type_counts):
        wall_flag = " << WALL" if t in WALL_TYPES else ""
        print(f"    Type {t:2d} ({OBJECT_TYPES.get(t, '?'):25s}): {type_counts[t]:4d}x{wall_flag}")
    
    # Unique IDs
    id_counts = {}
    id_types = {}
    for o in objects:
        id_counts[o['id']] = id_counts.get(o['id'], 0) + 1
        id_types.setdefault(o['id'], set()).add(o['type'])
    
    print(f"\n  UNIQUE OBJECT IDs: {len(id_counts)}")
    for oid in sorted(id_counts):
        types = sorted(id_types[oid])
        flag = ""
        if oid in MAHOGANY_IDS:
            flag = " << MAHOGANY TABLE"
        elif any(t in WALL_TYPES for t in types):
            flag = " << WALL"
        type_str = ",".join(str(t) for t in types)
        print(f"    ID {oid:5d}: {id_counts[oid]:3d}x  types=[{type_str}]{flag}")
    
    # --- Clean ---
    print(f"\n{'=' * 60}")
    print("STEP 3: Removing walls + mahogany tables")
    print("=" * 60)
    
    removed = {'walls': [], 'tables': [], 'kept': []}
    
    for obj in objects:
        if obj['type'] in WALL_TYPES:
            removed['walls'].append(obj)
        elif obj['id'] in MAHOGANY_IDS:
            removed['tables'].append(obj)
        else:
            removed['kept'].append(obj)
    
    print(f"  Walls removed:    {len(removed['walls'])}")
    print(f"  Tables removed:   {len(removed['tables'])}")
    print(f"  Objects kept:     {len(removed['kept'])}")
    
    # --- Save ---
    clean_data = encode_locations(removed['kept'])
    clean_path = os.path.join(WORK_DIR, "locations_8755_clean.dat")
    with open(clean_path, 'wb') as f:
        f.write(clean_data)
    
    print(f"\n{'=' * 60}")
    print("DONE!")
    print("=" * 60)
    print(f"  Original: {len(raw_data)} bytes, {len(objects)} placements")
    print(f"  Cleaned:  {len(clean_data)} bytes, {len(removed['kept'])} placements")
    print(f"  Saved to: {clean_path}")
    print(f"\n  Next: pack as file 945 in index 4 via HazyPacker")
    
    # Save report
    report_path = os.path.join(WORK_DIR, "cleanup_report.txt")
    with open(report_path, 'w') as f:
        f.write(f"Casino Cleanup Report - Region 8755\n{'='*40}\n\n")
        f.write(f"Walls removed: {len(removed['walls'])}\n")
        for w in removed['walls']:
            f.write(f"  ID={w['id']} ({w['x']},{w['y']}) plane={w['plane']} type={w['type']}\n")
        f.write(f"\nTables removed: {len(removed['tables'])}\n")
        for t in removed['tables']:
            f.write(f"  ID={t['id']} ({t['x']},{t['y']}) plane={t['plane']}\n")
        f.write(f"\nKept: {len(removed['kept'])}\n")
        for k in removed['kept']:
            f.write(f"  ID={k['id']} ({k['x']},{k['y']}) plane={k['plane']} type={k['type']}\n")
    print(f"  Report:  {report_path}")


if __name__ == '__main__':
    main()
