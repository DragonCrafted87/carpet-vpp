# Breakable blocks

These requirements describe the vanilla blocks Vanilla Plus Plus (`vpp`)
makes minable, and the silk-touch drops that go with them. They record
the current behavior.

The key words SHALL, SHALL NOT, RECOMMENDED, NOT RECOMMENDED, and
OPTIONAL in this document are to be interpreted as described in
RFC 2119.

## Destroy time and resistance

Bedrock SHALL have destroy time 75 and explosion resistance 2400.

Deepslate SHALL have destroy time 1.63. This mod SHALL NOT change
deepslate's explosion resistance.

An end portal frame SHALL have destroy time 6 and explosion resistance
2400\.

## Pickaxe tag

The mod SHALL append `minecraft:reinforced_deepslate` and
`minecraft:end_portal_frame` to `minecraft:mineable/pickaxe`. The tag
SHALL NOT replace the vanilla list. Budding amethyst is already on that
vanilla list.

## Silk Touch drops

Reinforced deepslate, an end portal frame, and budding amethyst SHALL
drop themselves only when the tool is a pickaxe and can apply Silk
Touch.

A pickaxe without Silk Touch SHALL drop nothing for those three blocks.
A Silk Touch tool that is not a pickaxe SHALL drop nothing for them.

A filled end portal frame SHALL drop the empty frame item under that
condition. It SHALL NOT drop the eye of ender.

Vanilla leaves the end portal frame loot table empty, and block drops
read that field directly. The frame's drops SHALL use
`minecraft:blocks/end_portal_frame`. A datapack file alone, or a loot
lookup the drop path never calls, does not supply that table.

This document does not give bedrock a special drop. No extra bedrock
loot table is part of the current implementation.
