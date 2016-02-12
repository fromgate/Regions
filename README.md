# Regions

Regions is a plugin for Nukkit, that provides useful private system for nukkit server.

You can define regions and control access rights to this regions: build or break blocks, accessing to chest, using levers, doors, etc.

## Commands

###Select area using right/left click
> /select​
> /sel​

Toggles selection mode. When selection mode enabled you can use left click (with "empty hand") and right click to select first and second point of future region.

###Select player location
> /select pos1
> /select p1
> /sel pos1
> /sel p1​

or

> /select pos2
> /select p2
> /sel pos2
> /sel p2​

Select player location as "point 1" or "point 2"

###Claim region
> /region claim [id]
> /rg claim [id]​

This command is allows players to claim region and become owner of the region. After claiming the region player will become owner of this region.

There's two modes for claiming.

1. Claim pre-defined regions.
To enable this modes set paramter of config.yml "claim.claim-only-existing-regions" to true.
Players must be able to claim regions predefined by define regions command.

2. Claim any area (limited by volume and allows regions per player value).

###Define regions
> /region define <id> [owner1][, owner2]
> /rg define <id> [owner1][, owner2]
> /rg create <id> [owner1][, owner2]
> /rg d <id>​[owner1][, owner2]

Create new region from your selection. This is admin command, usually you don't need to give access to this command to ordinary players 

###Redefine regions
> /region redefine <id>
> /rg redefine <id>
> /rg update <id>
> /rg move <id>​

Change region's area. Name, owners, members and flags will not change.

###Show region info
> /region info
> /rg info
> /rg i​

Show information about region in player location. 

###Show info about defined region
> /rg info <id>
> /rg i <id>​

Show information about defined region.

###List all regions
> /region list
> /rg list
> /rg lst​

Show list of available regions

###Configure flags of region
> /region flag <FLAG> [relation:<Relation] <value>
> /region f <FLAG> [rel:<Relation] <value>
> /rg flag <FLAG> [rel:<Relation] <value>
> /rg f <FLAG> [rel:<Relation] <value>​

Add flag to regions. Relations is a modificator 


Remove flag from region
/region flag <FLAG> clear
/rg f <FLAG> remove
/rg f <FLAG> rmv
/rg f <FLAG> delete
/rg f <FLAG> del​

Copy default flag to region flag
/region flag <FLAG> default
/rg f <FLAG> default
/rg f <FLAG> standart​

Show help
/region help
/rg help
/rg hlp​


Set owner(s) of region
/region setowner <id> <player 1>[, <player2>...]
/rg setowner <id> <player 1>[, <player2>...]
/rg setown <id> <player 1>[, <player2>...]
/rg so <id> <player 1>[, <player2>...]​

Add owner of regions
/region addowner <id> <player 1>[, <player2>...]
/rg addowner <id> <player 1>[, <player2>...]
/rg addown <id> <player 1>[, <player2>...]
/rg ao <id> <player 1>[, <player2>...]​

Remove owner from region
/region removeowner <id> <player>
/rg removeowner <id> <player>
/rg remowner <id> <player>
/rg remown <id> <player>
/rg ro <id> <player>​

Set member of region
/region setmember <id> <player 1>[, <player2>...]
/rg setmember <id> <player 1>[, <player2>...]
/rg setmem <id> <player 1>[, <player2>...]
/rg sm <id> <player 1>[, <player2>...]
​
Add member of regions
/region addmember <id> <player 1>[, <player2>...]
/rg addmember <id> <player 1>[, <player2>...]
/rg addmem <id> <player 1>[, <player2>...]
/rg am <id> <player 1>[, <player2>...]
​
Remove owner
/region removemember <id> <player>
/rg removemember <id> <player>
/rg remmember <id> <player>
/rg remmem <id> <player>
/rg rm <id> <player>
