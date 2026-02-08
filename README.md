# Cruel and Unusual
### A Mörk Borg Game

Cruel and Unusual is an independent production by Raphael Wise and is not affiliated with Ockult Örtmästare Games or Stockholm Kartell. It is published under the MÖRK BORG Third Party License.

## Building the Game
To build the game, a script called CruelAndUnusual.sh is provided, intended for test builds. Alternatively, the game uses maven as it's build system, and therefore one can alternitavly use <code>maven package</code> to build the game.
It is important to note that maven automaticly creates cruelandunusual-CruelAndUnusual.jar, which lacks a manifest, and can not be run
## Development
To develop game rules for Cruel and Unusual, which are the game's form of mods, read on.
No modification to the game's core should be necessary, however DEBUG can be passed as the last position dependent argument in CruelAndUnusual.sh to get debug output.