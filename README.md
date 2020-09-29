# Adventure Communist Analysis

This is a small Kotlin application to analyse which researchers are most
effective to upgrade in your current game state.

## Requirements
* Requires Java 11 to run

## Usage

* Update your game state (See section below)
* Run the application
    * Mac/Linux: `./run-analysis.sh`
    * Windows: `./run-analysis.bat`

# Game State
Update the `game-state.properties` file according to your game state
* Set the levels of your researchers
* Undiscovered researchers can be left blank or set to `null`
* If you like you can set the available cards for any researcher  
This would be recommended for your supremes and bonus researchers,
as they are the ones you would be likely to buy in the store for 
science. If you set the available cards for a researcher the cost
to buy his cards for the next upgrade are factored in.

  Syntax: `AvailableCards.<ResearcherName> = <Available Cards>`  
  Example: `AvailableCards.AlfStark = 2`

  If you are not interested in setting the available cards you can
  either delete the lines or comment them out (use `#`).

