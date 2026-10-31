# Bags

These requirements describe bags as implemented in Vanilla Plus Plus
(`vpp`). They record the current behavior.

The key words SHALL, SHALL NOT, RECOMMENDED, NOT RECOMMENDED, and
OPTIONAL in this document are to be interpreted as described in
RFC 2119.

## Items

The mod SHALL register `vpp:satchel_strap`, `vpp:bag_upgrade`,
`vpp:satchel`, `vpp:upgraded_satchel`, `vpp:pouch`, and
`vpp:upgraded_pouch`.

A satchel and an upgraded satchel SHALL equip in the Trinkets slot
`chest/satchel`. A pouch and an upgraded pouch SHALL equip in
`legs/pouch`. Using the item on that slot SHALL equip it.

A bag SHALL stack to 1. An upgraded satchel and an upgraded pouch SHALL
have rare rarity.

A normal satchel SHALL hold 9 stacks. An upgraded satchel SHALL hold 18.
A normal pouch SHALL hold 3 stacks. An upgraded pouch SHALL hold 6.
A satchel SHALL NOT be constructed with more than 18 slots. A pouch
SHALL NOT be constructed with more than 6.

The creative tab `vpp:itemgroup`, titled "vpp", SHALL offer all six
items. Its icon SHALL be a satchel.

## Trinket slots

The player entity SHALL enable `chest/satchel` with room for one bag and
`legs/pouch` with room for two bags. Those slot definitions SHALL NOT
replace Trinkets' other slots.

The satchel slot SHALL use the GUI sprite `vpp:container/slots/satchel`
and the name "Satchel". The pouch slot SHALL use
`vpp:container/slots/pouch` and the name "Pouch". Those sprites live
under `assets/vpp/textures/gui/sprites/` so the GUI atlas can load them.

A player SHALL NOT equip a second satchel. A player SHALL NOT equip a
third pouch. Replacing a bag with another bag of the same type SHALL
still be allowed.

## Inventory

The survival inventory SHALL add 18 satchel cells and two groups of 6
pouch cells. Cells past the equipped bag's capacity SHALL stay inactive.

Only the stacks the equipped bag can hold SHALL be active. The left
pouch is the pouch in Trinkets index 0. The right pouch is index 1.
The satchel uses the equipped satchel, ignoring that index.

Stored stacks SHALL be kept on the bag item in the container component.
The server SHALL write that component when a bag cell changes. The
client SHALL NOT write it.

A bag cell SHALL NOT accept a bag item.

When the equipped bag and the active cells disagree, the client SHALL
refresh the cells on the next client tick.

On join, the server SHALL refresh bag cells and SHALL send the empty
`vpp:enable_slots` payload. The client SHALL refresh bag cells when that
payload arrives and when the client joins.

Swapping one bag for another bag of the same item SHALL refresh cells
from the stack now in the slot. That path SHALL NOT depend on a separate
unequip and equip call. Trinkets treats a same-item replacement as a
stack change and skips those two calls.

When `keepInventory` is false, death SHALL detach the bag cells and
SHALL send `vpp:enable_slots`. Death SHALL NOT clear the container
component on the bag item. When `keepInventory` is true, death SHALL
leave the bag cells attached.

Transferring menu state SHALL skip a bag cell whose inventory is null.

After Trinkets rebuilds its own slots, the inventory menu SHALL renumber
every slot to its current list index.

When the recipe book is open and a left pouch is equipped, the client
SHALL shift the inventory screen right by 18 pixels for each pouch
column. A column is the slot count divided by 3, rounded up.

## Tooltip

A bag's tooltip SHALL include "Holds N items", with N in blue and the
line in gray.

When at least one stored stack is present, the tooltip SHALL show those
stacks. When every stored stack is empty, the tooltip SHALL NOT show
that grid.

The bag screen chrome SHALL use `vpp:textures/gui/slots.png`. That
texture is a direct GUI blit. It is not the Trinket slot sprite.

## Crafting

Datagen SHALL emit these recipes, and the mod jar SHALL contain them.

A pouch SHALL be a shaped recipe of string, leather, and a stone button
in the pattern `SLS` / `LbL` / `SLS`.

A satchel strap SHALL be a shapeless recipe of two leather and one
string.

A satchel SHALL be a shaped recipe of satchel straps, leather, string,
and a stone button in the pattern `TLT` / `LbL` / `SLS`.

A bag upgrade SHALL be a shaped recipe of four leather around one iron
ingot, in the pattern `L` / `LIL` / `L`. The item name SHALL be
"Bag Upgrade". Its model SHALL use `vpp:item/bag_upgrade`. It SHALL NOT
use the netherite smithing-template texture.

The smithing table SHALL upgrade a pouch with the bag upgrade in the
template slot and a diamond in the addition slot. The result SHALL be
an upgraded pouch. The recipe id SHALL be `upgraded_pouch`.

The same template and addition SHALL upgrade a satchel. The result SHALL
be an upgraded satchel. The recipe id SHALL be `upgraded_satchel`.

## Compatibility

`fabric.mod.json` SHALL break `infinitory`, `biginv`, `extrainv`,
`inventory_backpack`, `realistic-inventory`, and `inventorymod` at every
version.
