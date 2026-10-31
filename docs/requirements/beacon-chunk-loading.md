# Beacon chunk loading

These requirements describe beacon chunk tickets as implemented in
Vanilla Plus Plus (`vpp`). They record the current behavior.

The key words SHALL, SHALL NOT, RECOMMENDED, NOT RECOMMENDED, and
OPTIONAL in this document are to be interpreted as described in
RFC 2119.

## Ticket

The mod SHALL use one ticket type, `MinecraftVPP.BEACON`, with a
300-tick timeout. That type SHALL request loading, simulation, and
keeping the dimension active.

Loading alone keeps a chunk in memory and does not tick it. A ticket
that omits simulation or the dimension flag is NOT RECOMMENDED for this
feature. The beacon and a command block in range keep ticking only when
the ticket simulates, and the dimension stays active when no player is
in it.

The mod SHALL reuse this ticket type instance. It SHALL NOT register a
second type for the same job. Ticket storage matches tickets by type
identity.

## While the beacon runs

Every 80 game ticks, on the server, the mod SHALL read the beacon's
pyramid level.

When the beacon has a neighbor signal and the pyramid level is greater
than zero, the mod SHALL refresh the ticket at the beacon's chunk with
radius `1 + pyramid level`, and SHALL record that block position.

When the beacon has no neighbor signal, or the pyramid level is zero,
the mod SHALL remove that saved position. It SHALL NOT refresh the
ticket in that case.

## World load

Saved positions SHALL be stored per dimension under the saved-data id
`vpp:beacons`, in the field `activeBeacons`.

On world load, a saved position that is still a beacon block SHALL
receive a ticket of radius 5. A saved position that is no longer a
beacon block SHALL be removed.

The beacon debug log is a compile-time constant set to false. The
current classes SHALL NOT emit that log.
