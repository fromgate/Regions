# Regions
**Create private areas on your Nukkit server!**

Regions is a plugin for Nukkit, that provides useful private system for nukkit server. You can define regions and control access rights to this regions: build or break blocks, accessing to chest, using levers, doors, etc.

## Download
[**Release**](http://nukkit.ru/resources/regions.53/)

[**Latest dev build**  ![Snapshot](https://circleci.com/gh/fromgate/Regions.svg?style=shield&circle-token=2bb628276a494b5ac51ec0a0ae4deda896b6f3e3)](https://circleci.com/gh/fromgate/Regions)

## Features
* Regions management (create, redefine, configure region flags)
* World editing system (fill areas, replace blocks, copy areas, edit biomes)
* Brushes for easy world editing (set ball or discs, remove  blocks, set biome and biome color, smooth biomes, flood fill areas)
* Multilayer undo-system

## Commands
## Regions command
###Show help
>**/region help \[page\]**
>**/rg help \[page\]**
>**/rg hlp​\[page\]**

Show help.

###Select area using right/left click
>**/select**
>**/sel**

Toggles selection mode. When selection mode enabled you can use left click (with "empty hand") and right click to select first and second point of future region.

###Select player location
>**/select pos1**
>**/select p1**
>**/sel pos1**
>**/sel p1**

or

>**/select pos2**
>**/select p2**
>**/sel pos2**
>**/sel p2**

Select player location as "point 1" or "point 2"

###Claim region
>**/region claim \[id\]**
>**/rg claim \[id\]**

This command is allows players to claim region and become owner of the region. After claiming the region player will become owner of this region.

There's two modes for claiming.

1. Claim pre-defined regions.
To enable this modes set paramter of config.yml "claim.claim-only-existing-regions" to true.
Players must be able to claim regions predefined by define regions command.

2. Claim any area (limited by volume and allows regions per player value).

###Define regions
>**/region define \<id\> \[owner1\]\[, owner2\]**
>**/rg define \<id\> \[owner1\]\[, owner2\]**
>**/rg create \<id\> \[owner1\]\[, owner2\]**
>**/rg d \<id\>​\[owner1\]\[, owner2\]**

Create new region from your selection. This is admin command, usually you don't need to give access to this command to ordinary players 

###Redefine regions
>**/region redefine \<id\>**
>**/rg redefine \<id\>**
>**/rg update \<id\>**
>**/rg move \<id\>**

Change region's area. Name, owners, members and flags will not change.

###Show region info
>**/region info**
>**/rg info**
>**/rg i**

Show information about region in player location. 

###Show info about defined region
>**/rg info \<id\>**
>**/rg i \<id\>**

Show information about defined region.

###List all regions
>**/region list**
>**/rg list**
>**/rg lst**

Show list of available regions

###Configure flags of region
>**/region flag \<FLAG\> \[relation:\<Relation\] \<value\>**
>**/region f \<FLAG\> \[rel:\<Relation\] \<value\>**
>**/rg flag \<FLAG\> \[rel:\<Relation\] \<value\>**
>**/rg f \<FLAG\> \[rel:\<Relation\] \<value\>**

Add flag to regions. Relations is a modificator that filters players who will be affected by flag (ALL, OWNER,
MEMBER, NOT_MEMBER, NOT_OWNER)

### Flags
Here's list of supported flags:

* BREAK
* BUILD
* CHEST
* PVP
* LEVER
* PLATE
* DOOR
* TRAPDOOR
* GATE
* CLAIM
* ENTRY


###Remove flag from region
>**/region flag \<FLAG\> clear**
>**/rg f \<FLAG\> remove**
>**/rg f \<FLAG\> rmv**
>**/rg f \<FLAG\> delete**
>**/rg f \<FLAG\> del**

Remove flag from region. After deletion default flag configuration will affect to this region.

###Copy default flag to region flag
>**/region flag \<FLAG\> default**
>**/rg f \<FLAG\> default**
>**/rg f \<FLAG\> standart**

Create a new flag that equals to default flag. If server owner change a default parameter this values will not change.

###Set owner(s) of region
>**/region setowner \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg setowner \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg setown \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg so \<id\> \<player 1\>\[, \<player2\>...\]**

Set new owner(s) of region. Previous owner will be removed from region

###Add owner(s) to region
>**/region addowner \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg addowner \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg addown \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg ao \<id\> \<player 1\>\[, \<player2\>...\]**

Add one or more owners to region. 

###Remove owner from region
>**/region removeowner \<id\> \<player\>**
>**/rg removeowner \<id\> \<player\>**
>**/rg remowner \<id\> \<player\>**
>**/rg remown \<id\> \<player\>**
>**/rg ro \<id\> \<player\>**

Remove defined owner from region. 

###Set member of region
>**/region setmember \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg setmember \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg setmem \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg sm \<id\> \<player 1\>\[, \<player2\>...\]**

Set new member(s) of region. Previous members will removed from region
​
###Add member of regions
>**/region addmember \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg addmember \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg addmem \<id\> \<player 1\>\[, \<player2\>...\]**
>**/rg am \<id\> \<player 1\>\[, \<player2\>...\]**

Add one or more members to region.
​
###Remove owner
>**/region removemember \<id\> \<player\>**
>**/rg removemember \<id\> \<player\>**
>**/rg remmember \<id\> \<player\>**
>**/rg remmem \<id\> \<player\>**
>**/rg rm \<id\> \<player\>**

Remove defined member from region.

##World editing commands
###Fill area with provided block
>**/set \<block\>\[:\<data\>\]**
>**/blockset \<block\>\[:\<data\>\]**

Fill selected area with provided block. You can use block id or block name.

###Replace one block type to another
>**/replace \<block1\>\[:\<data1\>\] \<block2>\[:\<data2\>\]**
>**/blockreplace \<block1\>\[:\<data1\>\] \<block2>\[:\<data2\>\]**

Scan selected area and replace one type of blocks (block1) to another (block2)

###Build box
>**/box \<block\>\[:\<data\>\]**
>**/buildbox \<block\>\[:\<data\>\]**

Use selection to build box

###Build walls
>**/walls \<block\>\[:\<data\>\]**
>**/buildwalls \<block\>\[:\<data\>\]**

Use selection to build walls

###Copy and paste selections
>**/copy**

Copy selected blocks into a clipboard

>**/paste**

Paste blocks from the clipboard in a world

###Biome operations
>**/biome**
>**/wm**

Show information about biome in player location

>**/biome list**
>**/wm list**

Show all available biomes

###Set biome or grass color
>**/biome \<biome|color\>**
>**/wm \<biome|color\>**
>**/biome \<biome\> \<color\>**
>**/wm \<biome\> \<color\>**

Set biome or/and grass color for selected area. 

Supported biome list could be found use using ``/biome list`` command.

Supported colors: White, Orange, Magenta, Light Blue, Yellow, Lime,
Pink, Gray, Light Gray, Cyan, Purple, Blue, Brown, Green, Red, Black.
(Space in name of color must be replaced with character "_").

And you can use decimal RGB-values to define color.

Examples:
```
/biome swamp
/biome swamp red
/biome light_blue
/biome plains 125,100,255
```


###Smooth biome colors
>**/biome smooth \[radius\]**
>**/wm smooth \[radius\]**

Smooth color transition between different grass colors.
 
##Brush
###Show current brush info
>**/brush**

Display current brush name and brush settings

###Give brush
>**/brush give**

Give player brush item. Item type, data and name configured at config.yml

###Select brush mode
>**/brush \<brushType\> radius \<radius\> \[<paramter> <value>\] ...**

Supported brush types:

* **BUILD** — place a sphere or circle of blocks.  
Supported parameters:
    * block <type>[:data] — block type that will used to create sphere
    * radius <radius> — radius of sphere
    * type <ball|disk> — place sphere or circle of blocks
* **BREAK** — remove blocks from the world  
Supported parameters:
    * block <type>[:data] — block type that will removed
    * radius <radius> — radius of sphere
    * type <ball|disk> — remove sphere or circle of blocks
* **FILL** — fill areas using flood fill algorithm. Fill distance 
horizontally limited by radius, but vertically (down) will fall as far as possible  
Supported parameters:
    * block <type>[:data] — block type that will used to place
    * radius <radius> — radius of sphere
* **BIOME** — set biome or/and grass color  
Supported parameters:
    * biome <biomeName> — biome name (spce replaced with "_")
    * color <colorName|r,g,b> — grass color
    * radius <radius> — brush size
* **SMOOTH** — make grass color transition softer
    * radius <radius> — brush size
    * size <size> — matrix size (by default equal to radius)
    
    