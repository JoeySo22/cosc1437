

import java.io.*;
import java.util.Scanner;

public class Engine 
{
  public static void main(String[] args) throws FileNotFoundException
  {
    // The engine will require a map text file in the args
    if (args.length != 0)
    {
      
      // Every class gets instantiated.
      Scanner keyboard = new Scanner(System.in);
      File the_map_file = new File(args[0]);
      Grid grid = new Grid(the_map_file);
      PacMan pacman = new PacMan(grid.getPlayerLocation());
      
      //This is the main loop of the engine.
      while (true)
      {
        //Every loop will print the grid.
        grid.printGrid();
        //We have to count the cookies after every part of the loop. This is necessary because the portals 
        //made the eliminating more complicated. 
        grid.countCookiesOnMap();
        //For user's sake, we print the cookies.
        System.out.println(grid.returnCookieCount());
        
        //A simple check after the map and cookies are made if there are anymore cookies. If we ate them all the
        // game is over.
        if (grid.isGameOver()) 
        {
          System.out.println("Game Over");
          break;
        }
        
        //Now we create a variable to manipulate for each branch of possible inputs.
        String player_input = keyboard.nextLine();
        
        // If the user just pressed enter when prompted. we just loop again.
        if (player_input.equals(""))
        {
          continue;
        }
        
        // if the user pressed space, we just continue;
        else if (player_input.substring(0,1).equals(" "))
        {
          continue;
        }
        
        // if the user puts in q for quit, we end the game.
        else if (player_input.substring(0,1).equals("q"))
        {
          System.out.println("Player quit.");
          break;
        }
        
        // We now enter the main logic of the engine.
        else
        {
          //We calculate what the next spot is after the user enters his choice. With this we are able to
          //calculate the next moves and where it would land them by taking the current location and the input.
          int[] next_spot = calculateNextCoordinates(player_input.substring(0,1), grid.getPlayerLocation());
          
          //checks next empty spot is one of the portals, if it is it transports, if not it just goes into the spot.
          if (checkNextSpace(grid, next_spot).equals(" "))
          {
            //We check if the next spot is a portal on the grid.
            if (checkPortal(grid, next_spot))
            {
              // Verify the result of the action by printing the reaction to the map.
              System.out.println("Found a portal");
              // We need to constantly check the portals to see what to do about the players move, so we create 
              // a variable.
              int[][] list_of_portals = grid.returnPortals();
              
              // We compare the users moves to the portals and decide where the player lands
              if (next_spot[0] == list_of_portals[0][0] & next_spot[1] == list_of_portals[0][1])
              {
                //We create this special array for correction using the transport function.
                int[] coordcorrection = {0,-1};
                //We transport the pacman according to the specific portal and pass a coordcorrection.
                pacman.transportPacMan(list_of_portals[3], coordcorrection);
                //We update the Strings on the map.
                grid.updateMap(pacman.location());
              }
              
              if (next_spot[0] == list_of_portals[1][0] & next_spot[1] == list_of_portals[1][1]) 
              {
                int[] coordcorrection = {1,-1};
                pacman.transportPacMan(list_of_portals[2], coordcorrection);
                grid.updateMap(pacman.location());
              }
              
              if (next_spot[0] == list_of_portals[2][0] & next_spot[1] == list_of_portals[2][1]) 
              {
                int[] coordcorrection = {1,1};
                pacman.transportPacMan(list_of_portals[1], coordcorrection);
                grid.updateMap(pacman.location());
              }
              
              if (next_spot[0] == list_of_portals[3][0] & next_spot[1] == list_of_portals[3][1]) 
              {
                int[] coordcorrection = {0,1};
                pacman.transportPacMan(list_of_portals[0], coordcorrection);
                grid.updateMap(pacman.location());
              }
              
            }
            // If the space is just empty because we ate a cookie, we just move like normal.
            else
            {
              pacman.movePacMan(player_input.substring(0,1));
              grid.updateMap(pacman.location());
            }
          }
          
          //Again we just move like normal if we find a cookie and we just update again.
          if (checkNextSpace(grid, next_spot).equals("."))
          {
            System.out.println("Found a cookie");
            pacman.movePacMan(player_input.substring(0,1));
            grid.updateMap(pacman.location());
          }
          
          //We reject any change if the user goes into a wall, at least we let him know.
          if (checkNextSpace(grid, next_spot).equals("X"))
          {  
            System.out.println("Found a wall");
            continue;
          }
        }
      }
    }
    
    //Finally the else statement where we specificy what this program needs.
    else System.out.print("Error: Needs (1) maze txt file. Ex( \"java Engine maze.txt\" ) ");
    
  }
  
  //function for figuring out the next move. 
  private static int[] calculateNextCoordinates(String nextmove, int[] current_space)
  {
    int[] desired_space = new int[2];
    if (nextmove.equals("w")) 
    {
      desired_space[0] = current_space[0] - 1;
      desired_space[1] = current_space[1];
    }
    else if (nextmove.equals("a"))
    {
      desired_space[0] = current_space[0];
      desired_space[1] = current_space[1] - 1;
    }
    else if (nextmove.equals("s")) 
    {
      desired_space[0] = current_space[0] + 1;
      desired_space[1] = current_space[1];
    }
    else if (nextmove.equals("d")) 
    {
      desired_space[0] = current_space[0];
      desired_space[1] = current_space[1] + 1;
    }
    return desired_space;
  }
  
  //A function for checking the desired move ahead and returns what the String is in that coordinate. 
  private static String checkNextSpace(Grid a_grid, int[] desired_move)
  {
    return a_grid.returnGrid()[desired_move[0]][desired_move[1]];
  }
  
  //A function for seeing if a set of coordinates is one of the portals.
  private static boolean checkPortal(Grid grid, int[] coords)
  {
    boolean in_portal = false;
    for (int x = 0; x < grid.returnPortals().length; x++)
    {
      if (grid.returnPortals()[x][0] == coords[0] & grid.returnPortals()[x][1] == coords[1])
      {
        in_portal = true;
      }
    }
    return in_portal;
  }
  
  //This is our nested Grid class.
  static class Grid
  {
    //This is our main grid here that is used often.
    private String[][] strGrid;
    //This is the cookie counter for all the "." on strGrid.
    private int iCookieCount = 0;
    //This is the mapfile the object receives. 
    private final File map_file;
    //We instantiate a scanner that we have to constantly renew with a new object for looping.
    private Scanner scanner_map;
    //This is the coordinates of the players current location.
    private int[] player_location = new int[2];
    //This is the portal list that is populated when we parse the map. We hardcode it to be only 4 portals.
    private int[][] portal_list = new int[4][2];
    
    //This is our constructor that takes the File and does all the major to make our grid.
    public Grid(File gridMap) throws FileNotFoundException
    {
      this.map_file = gridMap;
      this.scanner_map = makeScanner(gridMap);
      this.strGrid = parseMap(scanner_map);
    }
    
    //Getter method for the cookies.
    public int returnCookieCount()
    {
      return this.iCookieCount;
    }
    
    //function that counts the cookies on the instantiated objects cookies on the grid.
    public void countCookiesOnMap()
    {
      this.iCookieCount = 0;
      for (int x = 0; x < this.strGrid.length; x++)
      {
        for (int y = 0; y < this.strGrid.length; y++)
        {
          if (this.strGrid[x][y].equals("."))
          {
            this.iCookieCount++;
          }
        }
      }
    }
    
    //Tells us if the game is over when the Cookies are 0.
    public boolean isGameOver()
    {
      if (this.iCookieCount == 0)
      {
        return true;
      }
      else return false;
    }
    
    //getter method for portal list.
    public int[][] returnPortals()
    {
      return this.portal_list;
    }
    
    //boolean function to tell us whether or not the next move is a portal or not.
    public boolean checkPortals(int [] coords)
    {
       boolean isPortal = false;
       for (int x = 0; x < this.portal_list.length; x++)
       {
         if (this.portal_list[x][0] == coords[0] & this.portal_list[x][1] == coords[1])
           isPortal = true;
       }
       return isPortal;
    }
    
    //getter method for our grid.
    public String[][] returnGrid()
    {
      return this.strGrid;
    }

    //We update the Grid and the Location of the player.
    public void updateMap(int[] player_update)
    {
      this.strGrid[player_location[0]][player_location[1]] = " ";
      this.player_location[0] = player_update[0];
      this.player_location[1] = player_update[1];
      this.strGrid[player_update[0]][player_update[1]] = "e";
    }
    
    //function that prints the String grid.
    public void printGrid()
    {
      for (int x = 0; x < strGrid.length; x++)
      {
        for (int y = 0; y < strGrid[0].length; y++)
        {
          System.out.print(strGrid[x][y]);
        }
        System.out.print("\n");
      }
    }
    
    //This is our function that takes the File/Scanner reader and creates our String Grid.
    private String[][] parseMap(Scanner inputMap) throws FileNotFoundException
    {
      String[][] parsedMap;
      int intMapHeight;
      int intMapLength = inputMap.nextLine().length();
      //Scanner has to restart by creating a new scanner after each .nextLine() or .hasNextLine() method.
      inputMap = makeScanner(this.map_file);
      
      //We have to create a special loop to count the height of the grid. Scanner doesn't have a function to count lines.
      for (intMapHeight = 0; inputMap.hasNextLine(); intMapHeight++)
      {
        inputMap.nextLine();
      }
      //Restart Scanner
      inputMap = makeScanner(this.map_file);
      
      //We finally create our 2D array  
      parsedMap = new String[intMapHeight][inputMap.nextLine().length()];
      //We used .nextLine() again in parsedMap, restart scanner
      inputMap = makeScanner(this.map_file);
      
      //We begin our portal scanner. We will now populate the official strGrid with help from parsedMap
      int portal_counter = 0;
      for (int x = 0; x < intMapHeight; x++)
      {
        //We have to take scanners .nextLine() string and parse each character to fill the 2D array.
        String row = inputMap.nextLine();
        for (int y = 0; y < row.length(); y++)
        {
          //We convert the Character to String because our "row" string only has a charAt that is easy to use.
          //We then just convert to string.
          parsedMap[x][y] = Character.toString(row.charAt(y));
          
          //We locate our player from the file as "e" and set our player location.
          if (parsedMap[x][y].equals("e")) 
          {
            player_location[0] = x;
            player_location[1] = y;
          }
          //We locate our portals and fill in our portal array.
          if (parsedMap[x][y].equals(" "))
          {
            this.portal_list[portal_counter][0] = x;
            this.portal_list[portal_counter][1] = y;
            portal_counter++;
          }
        }
      }
      // We finally return our 2D array created by this function from the file.
      return parsedMap;
    }
    
    //function that creates a new scanner with the same file.
    private Scanner makeScanner(File the_file) throws FileNotFoundException
    {
      return new Scanner(the_file);
    }
    
    //Getter method for the player location.
    public int[] getPlayerLocation()
    {
      return this.player_location;
    }

  }
  
  //This is our nested Pacman class
  static class PacMan
  {
    // first/0 is X and second/1 is Y
    private int[] intCords = new int[2];
    
    //Constructor for Pacman. Takes coords as its position.
    public PacMan(int[] startingPosition)
    {
      this.intCords[0] = startingPosition[0];
      this.intCords[1] = startingPosition[1];
    }
    
    // Simple function that updates the players coordinates with its recieved move.
    public void movePacMan(String move)
    {
      switch (move.substring(0,1))
      {
        case "w": 
          intCords[0] -= 1;
          break;
        case "s":
          intCords[0] += 1;
          break;
        case "d":
          intCords[1] += 1;
          break;
        case "a":
          intCords[1] -= 1;
          break;
        default: 
          break;
      }
    }
    
    // For portal use, we need to transport the pacman somehow.
    public void transportPacMan(int[] newSpot, int[] correction)
    {
      if (correction[0] == 0)
      {
        intCords[0] = newSpot[0] + correction[1];
        intCords[1] = newSpot[1];
      }
      else if (correction[0] == 1)
      {
        intCords[0] = newSpot[0];
        intCords[1] = newSpot[1] + correction[1];
      }
    }
    
    //getter method for the coords.
    public int[] location()
    {
      return intCords;
    }
  }
}
