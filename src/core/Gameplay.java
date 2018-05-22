/*
 * Alan Halvey - 14465722
 * Alan Holmes - 14719591
 * Greg Sloggett - 14522247
 */
package core;
import java.awt.Color;

public class Gameplay {

	//Determines whether a player has reinforcements left or not. Returns the result of true or false.
	public static boolean reinforcementsLeft(String player){
		boolean result = true;
		for(int k = 0;k<42;k++){
			if(player.compareToIgnoreCase(Deck.countriesAfterShuffle[k].getOccupyingPlayer().playerName)==0){
				if(player.compareTo(CommandInput.getPlayer2())==0 && Deck.player2.numReinforcements ==0){
					result = false;
				}
				if(player.compareTo(CommandInput.getPlayer1())==0 && Deck.player1.numReinforcements ==0){
					result = false;
				}
			}
		}
		return result;
	}

	//Function allows players to place their reinforcements, with error handling.
	static void placeReinforcements(String currentPlayer){
		//if the current player is player 1 ..
		if(currentPlayer.compareTo(CommandInput.player1)==0){
			if(Gameplay.reinforcementsLeft(CommandInput.player1)==true){
				CommandInput.appendStringTo(currentPlayer + ", please type the country name to place Reinforcements in: \n", Color.BLACK);
				String country = CommandInput.getCommand();
				CommandInput.appendStringTo(country+"\n.", Color.BLACK);
				boolean countryCheck = CommandInput.countryCheck(country);
				if(countryCheck){
					ErrorHandling.placeReinforcementsErrorChecksP1(country);
				}
				else{
					CommandInput.appendStringTo("Invalid input. Try again.\n", Color.RED);
					placeReinforcements(currentPlayer);
				}
			}
			else{
				CommandInput.appendStringTo(currentPlayer + " has no reinforcements left, placing reinforcements.\n", Color.RED);

			}
		}

		//If the current player is player 2 ..
		if(currentPlayer.compareTo(CommandInput.player2)==0){
			if(Gameplay.reinforcementsLeft(CommandInput.player2)==true){
				CommandInput.appendStringTo(currentPlayer + ", please type the country name to place Reinforcements in: \n", Color.BLACK);
				String country = CommandInput.getCommand();
				CommandInput.appendStringTo(country+"\n.", Color.BLACK);
				boolean countryCheck = CommandInput.countryCheck(country);
				if(countryCheck){
					ErrorHandling.placeReinforcementsErrorChecksP2(country);
				}
				else{
					CommandInput.appendStringTo("Invalid input. Try again.\n", Color.RED);
					placeReinforcements(currentPlayer);
				}
			}
			else{
				CommandInput.appendStringTo(currentPlayer + " has no reinforcements left, skipping placing reinforcements.\n", Color.RED);
			}
		}	
	}

	//calculates the reinforcements each player has left to place
	static void calculateReinforcements() {
		boolean alreadyAdded = false;
		for(int i = 0;i<42;i++){
			if(alreadyAdded == false){
				if(Deck.countriesAfterShuffle[i].getOccupyingPlayer().playerName.compareTo(CommandInput.currentPlayer)==0){
					//if the current player has armies he gets 3 armies to place. 
					//This is mandatory every turn, as long as the player is still in the game.
					if(Deck.calculateTerritories(CommandInput.currentPlayer) <=9 ){
						if(CommandInput.currentPlayer.compareTo(CommandInput.getPlayer1())==0){
							Deck.player1.numReinforcements += 3;
							alreadyAdded = true;
						}
						if(CommandInput.currentPlayer.compareTo(CommandInput.getPlayer2())==0){
							Deck.player2.numReinforcements += 3;
							alreadyAdded = true;
						}
					}
					//calls calculate territories function. If the number of territories owned is greater 
					//than 9 then the player gets the value of the number of territories he owns divided by 3
					else if(Deck.calculateTerritories(CommandInput.currentPlayer) > 9){
						if(CommandInput.currentPlayer.compareTo(CommandInput.getPlayer1())==0){
							Deck.player1.numReinforcements+= Deck.calculateTerritories(CommandInput.currentPlayer) / 3;
							alreadyAdded = true;
						}
						if(CommandInput.currentPlayer.compareTo(CommandInput.getPlayer2())==0){
							Deck.player2.numReinforcements += Deck.calculateTerritories(CommandInput.currentPlayer) / 3;
							alreadyAdded = true;
						}
					}
				}
			}
		}
	}

	//Function allowing the fortification between territories for a player. Once per go..
public static void Fortify(Country takeArmies, Country putArmies, int amountMoved){
		
		if(takeArmies.getPlayerArmies()<=1 || amountMoved > takeArmies.getPlayerArmies() ){
			CommandInput.appendStringTo("You do not have enough armies to do this foritfy\n", Color.RED);
			TurnSequence.Fortify();
		}

		else{


			if(Map.arrayContains(takeArmies.getAdjacent(), putArmies.getIndex()) && takeArmies.getOccupyingPlayer().playerName.compareTo(putArmies.getOccupyingPlayer().playerName)==0 && takeArmies.getOccupyingPlayer().fortified==true){
				System.out.println("blahblahblahblah");

				takeArmies.setPlayerArmies(takeArmies.getPlayerArmies()-amountMoved);
				putArmies.setPlayerArmies(putArmies.getPlayerArmies()+amountMoved);
				CommandInput.appendStringTo(takeArmies.getName() + " now has " + takeArmies.getPlayerArmies() + " units.\n", Color.RED);
				CommandInput.appendStringTo(putArmies.getName() + " now has " + putArmies.getPlayerArmies() + " units.\n", Color.RED);
				takeArmies.getOccupyingPlayer().fortified=false;
				Screen.mainFrame.repaint();
			}

			else{
				CommandInput.appendStringTo("This in an Invalid Entry, please ensure that both the countries you have entered are adjacent.\n",Color.BLACK);
				TurnSequence.Fortify();
			}
		}
	}

	//Finds the country associated with the abbreviation from the user input
	public static String setFromAbbreviation(String country) {
		for(int i = 0;i<42;i++){
			if(country.compareToIgnoreCase(Deck.countriesAfterShuffle[i].getAbbreviation())==0){
				return Deck.countriesAfterShuffle[i].getName();
			}
			if(country.compareToIgnoreCase(Deck.countriesAfterShuffle[i].getName())==0){
				return Deck.countriesAfterShuffle[i].getName();
			}
		}
		return null;
	}

	//Takes a String and makes a country object from it.
	public static Country setCountry(String country) {
		for(int i =0;i<42;i++){
			if (country.compareToIgnoreCase(Deck.countriesAfterShuffle[i].getName())==0){
				return Deck.countriesAfterShuffle[i];
			}
		}
		return null;
	}

	//Exchange function to allow exchange of territory/insignia cards
	public static int Exchange(String currentPlayer, int currentPlayerTerritoryCards) {
		Color playerColor;
		if(currentPlayer.compareTo(CommandInput.player1)==0){
			playerColor = Color.BLUE;
		}
		else{
			playerColor = Color.MAGENTA;
		}
		String input;
		CommandInput.appendStringTo(CommandInput.currentPlayer+" ,please enter the first letters of the three insignias you wish to exchange\n." ,playerColor);
		CommandInput.appendStringTo("The options are: 'aaa', 'ccc', 'iii' or one of each insignia.\n", playerColor);
		do{
			input = CommandInput.getCommand();
			if(input.length()!=3 || (getInsigniaValue(input)==0)){
				CommandInput.appendStringTo("Invalid input. Try again.\n", Color.RED);
			}
			if(InsigniaChecks(input)==false){
				CommandInput.appendStringTo("You don't have enough territory cards with those insignias.", Color.RED);
			}

		}while(input.length()!=3 || getInsigniaValue(input)==0 || InsigniaChecks(input)==false);

		int additionalReinforcements = 0;


		currentPlayerTerritoryCards-=3;


		if(Data.numberOfDeals==14){
			Data.numberOfDeals=0;
		}

		if(Data.numberOfDeals<=3){
			additionalReinforcements=4;
			for(int i=0;i<Data.numberOfDeals;i++){
				additionalReinforcements+= 2;
			}
		}
		if(Data.numberOfDeals>3){
			additionalReinforcements=10;
			for(int i= 3 ; i<Data.numberOfDeals;i++){
				additionalReinforcements+=5;
			}
		}
		Data.numberOfDeals++;
		CommandInput.appendStringTo((additionalReinforcements + "additional reinforcements\n"),Color.RED);


		int removedCardsCount = 0;
		boolean artillaryRemoved = false;
		boolean cavalryRemoved = false;
		boolean infantryRemoved = false;

		if(currentPlayer.compareTo(Deck.player1.playerName)==0){
			Deck.player1.numReinforcements+= additionalReinforcements;
		}
		if(currentPlayer.compareTo(Deck.player2.playerName)==0){
			Deck.player2.numReinforcements+= additionalReinforcements;
		}

		for(int i=0;i< TerritoryCard.territoryCardsShuffled.size();i++){
			if(removedCardsCount <3){
				if(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName.compareTo(CommandInput.currentPlayer)==0 && getInsigniaValue(input)==1 && TerritoryCard.territoryCardsShuffled.get(i).getCardName().compareToIgnoreCase("artillary")==0){
					TerritoryCard.territoryCardsShuffled.remove(i);
					removedCardsCount++;
				}
				if(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName.compareTo(CommandInput.currentPlayer)==0 && getInsigniaValue(input)==2 && TerritoryCard.territoryCardsShuffled.get(i).getCardName().compareToIgnoreCase("cavalry")==0){
					TerritoryCard.territoryCardsShuffled.remove(i);
					removedCardsCount++;
				}
				if(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName.compareTo(CommandInput.currentPlayer)==0 && getInsigniaValue(input)==3 && TerritoryCard.territoryCardsShuffled.get(i).getCardName().compareToIgnoreCase("infantry")==0){
					TerritoryCard.territoryCardsShuffled.remove(i);	
					removedCardsCount++;
				}
				if(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName.compareTo(CommandInput.currentPlayer)==0 && getInsigniaValue(input)==4 && TerritoryCard.territoryCardsShuffled.get(i).getCardName().compareToIgnoreCase("artillary")==0 && !(artillaryRemoved)){
					TerritoryCard.territoryCardsShuffled.remove(i);
					removedCardsCount++;	
					artillaryRemoved = true;
				}
				if(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName.compareTo(CommandInput.currentPlayer)==0 && getInsigniaValue(input)==4 && TerritoryCard.territoryCardsShuffled.get(i).getCardName().compareToIgnoreCase("cavalry")==0 && !(cavalryRemoved)){
					TerritoryCard.territoryCardsShuffled.remove(i);
					removedCardsCount++;
					cavalryRemoved = true;
				}
				if(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName.compareTo(CommandInput.currentPlayer)==0 && getInsigniaValue(input)==4 && TerritoryCard.territoryCardsShuffled.get(i).getCardName().compareToIgnoreCase("infantry")==0 && !(infantryRemoved)){
					TerritoryCard.territoryCardsShuffled.remove(i);
					removedCardsCount++;	
					infantryRemoved = true;
				}
			}
		}
		Data.alreadyExchanged=false;
		return currentPlayerTerritoryCards;
	}

	public static boolean InsigniaChecks(String input) {
		int[] counts = new int[3];
		counts = getCounts();

		int artilleryCount = counts[0];

		int cavalryCount = counts[1];
		int infantryCount = counts[2];


		if(getInsigniaValue(input)==1 && artilleryCount>=3){
			return true;
		}
		if(getInsigniaValue(input)==2 && cavalryCount>=3){
			return true;
		}
		if(getInsigniaValue(input)==3 && infantryCount>=3){
			return true;
		}
		if(getInsigniaValue(input)==4 && infantryCount >= 1 && artilleryCount >= 1 && cavalryCount >= 1){
			return true;
		}
		else{
			return false;
		}
	}

	static int[] getCounts() {
		int[] counts = {0,0,0};
		for(int i =0;i<TerritoryCard.territoryCardsShuffled.size();i++){
			System.out.println(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName);

			if (CommandInput.currentPlayer.compareToIgnoreCase(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName) == 0 &&  ("Artillary").compareToIgnoreCase(TerritoryCard.territoryCardsShuffled.get(i).getCardName()) == 0){	
				counts[0]++;
				System.out.println(counts[0] + " artillery");}
			if (CommandInput.currentPlayer.compareToIgnoreCase(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName) == 0 &&  ("cavalry").compareToIgnoreCase(TerritoryCard.territoryCardsShuffled.get(i).getCardName()) == 0){	
				counts[1]++;
				System.out.println(counts[1] + " cavalry");}
			if (CommandInput.currentPlayer.compareToIgnoreCase(TerritoryCard.territoryCardsShuffled.get(i).getPlayer().playerName) == 0 &&  ("infantry").compareToIgnoreCase(TerritoryCard.territoryCardsShuffled.get(i).getCardName()) == 0){	
				counts[2]++;
				System.out.println(counts[2] + " infantry");}
		}
		return counts;
	}

	private static int getInsigniaValue(String input) {

		if(input.compareToIgnoreCase("aaa")==0 ){
			return 1;

		}
		if(input.compareToIgnoreCase("ccc")==0 ){
			return 2;

		}
		if(input.compareToIgnoreCase("iii")==0 ){
			return 3;
		}
		if(input.compareToIgnoreCase("ica")==0 || input.compareTo("cia")==0 || input.compareTo("aic")==0 || input.compareTo("cai")==0 || input.compareTo("aci")==0 || input.compareTo("iac")==0 ){
			return 4;
		}
		else{
			System.out.println("it was false");
			return 0;
		}

	}

}