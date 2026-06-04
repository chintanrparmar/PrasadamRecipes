#!/usr/bin/env python3
"""Merge all batch JSON files into recipes.json"""
import json, glob, sys, os

base = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
main_file = os.path.join(base, "app/src/main/assets/recipes.json")

with open(main_file) as f:
    data = json.load(f)

existing_ids = {r["id"] for r in data["recipes"]}
existing_slugs = {r["slug"] for r in data["recipes"]}
added = 0

for batch_file in sorted(glob.glob(os.path.join(os.path.dirname(__file__), "batch_*.json"))):
    with open(batch_file) as f:
        batch = json.load(f)
    for r in batch:
        if r["id"] in existing_ids:
            print(f"  SKIP dup id={r['id']}", file=sys.stderr); continue
        if r["slug"] in existing_slugs:
            print(f"  SKIP dup slug={r['slug']}", file=sys.stderr); continue
        data["recipes"].append(r)
        existing_ids.add(r["id"])
        existing_slugs.add(r["slug"])
        added += 1

data["recipes"].sort(key=lambda r: r["id"])

with open(main_file, "w", encoding="utf-8") as f:
    json.dump(data, f, ensure_ascii=False, indent=2)

print(f"Added {added} recipes. Total now: {len(data['recipes'])}")
