# Suspicious stew

These requirements describe suspicious-stew feedback as implemented in
Vanilla Plus Plus (`vpp`). They record the current behavior.

The key words SHALL, SHALL NOT, RECOMMENDED, NOT RECOMMENDED, and
OPTIONAL in this document are to be interpreted as described in
RFC 2119.

## Tooltip

For a suspicious stew, the item tooltip SHALL append one gray line per
effect. Each line SHALL be the effect name, a space, and the duration
formatted at 20 ticks per second.

A stack that is not a suspicious stew SHALL NOT gain those lines.

The stew debug log is a compile-time constant set to false. The current
classes SHALL NOT emit that log.

## Hotbar lines

While the held-item highlight timer is above zero and the main hand is
not empty, the client SHALL draw those effect lines above the hotbar.
The element SHALL be attached after the vanilla held-item tooltip.
Vanilla still draws the item name. This element SHALL draw a background
bar sized to that hover name, and SHALL draw each effect line above
that bar.

The line position SHALL start at `guiScaledHeight - 59`. Creative mode
and spectator mode SHALL add 14 to that position. Adventure mode SHALL
stay on the survival position.

Creative mode SHALL be `GameType.CREATIVE` from the client game mode.
Spectator mode SHALL be the client game mode's spectator query. The
offset SHALL NOT treat adventure as creative. Using the survival-only
experience check for this offset is NOT RECOMMENDED, because that check
classifies adventure with creative.

## Highlight timer

Vanilla restarts the held-item name only when the item type or the hover
name changes. Every suspicious stew shares both, so the name and the
effect lines stay up until the player leaves suspicious stews.

When the selected stack and the previous highlight are both suspicious
stews, and their effect lists differ by effect or by duration, the
client SHALL clear the previous highlight before the vanilla timer
runs. That clear SHALL make the name and the effect lines start again.

When either stack is not a suspicious stew, this check SHALL NOT clear
the highlight. When both stews have the same effects and the same
durations, this check SHALL NOT clear the highlight.
