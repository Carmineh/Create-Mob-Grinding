import os
import json

base_dir = "src/main/resources/data/createmobgrinding/recipe/sequenced_assembly"
os.makedirs(base_dir, exist_ok=True)

mobs = {
    "zombie": ("minecraft:rotten_flesh", False),
    "skeleton": ("minecraft:bone", False),
    "creeper": ("minecraft:gunpowder", False),
    "spider": ("minecraft:spider_eye", False),
    "enderman": ("minecraft:ender_pearl", True),
    "blaze": ("minecraft:blaze_rod", True),
    "wither_skeleton": ("minecraft:wither_skeleton_skull", True),
    "wither": ("minecraft:nether_star", True),
    "piglin": ("minecraft:gold_ingot", False)
}

for mob_id, data in mobs.items():
    ingredient = data[0]
    exp_item = "create:experience_block" if data[1] else "create:experience_nugget"
    
    recipe = {
      "type": "create:sequenced_assembly",
      "ingredient": {
        "item": "createmobgrinding:blank_spawner_chunk"
      },
      "loops": 5,
      "results": [
        {
          "chance": 80.0,
          "id": "createmobgrinding:mob_spawner_chunk",
          "components": {
            "createmobgrinding:spawner_entity": f"minecraft:{mob_id}"
          }
        },
        {
          "chance": 20.0,
          "id": "createmobgrinding:unfinished_spawner_chunk"
        }
      ],
      "sequence": [
        {
          "type": "create:deploying",
          "ingredients": [
            {
              "item": "createmobgrinding:unfinished_spawner_chunk"
            },
            {
              "item": ingredient
            }
          ],
          "results": [
            {
              "id": "createmobgrinding:unfinished_spawner_chunk"
            }
          ]
        },
        {
          "type": "create:deploying",
          "ingredients": [
            {
              "item": "createmobgrinding:unfinished_spawner_chunk"
            },
            {
              "item": exp_item
            }
          ],
          "results": [
            {
              "id": "createmobgrinding:unfinished_spawner_chunk"
            }
          ]
        },
        {
          "type": "create:pressing",
          "ingredients": [
            {
              "item": "createmobgrinding:unfinished_spawner_chunk"
            }
          ],
          "results": [
            {
              "id": "createmobgrinding:unfinished_spawner_chunk"
            }
          ]
        }
      ],
      "transitional_item": {
        "id": "createmobgrinding:unfinished_spawner_chunk"
      }
    }
    
    with open(os.path.join(base_dir, f"chunk_{mob_id}.json"), "w") as f:
        json.dump(recipe, f, indent=2)

print("Generated all recipes!")
