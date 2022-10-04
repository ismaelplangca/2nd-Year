package Lab9Graphs;

import java.util.*;

public class Lab9 {
    public static void solution() {
        int lives = 200;
        Scanner myScanner = new Scanner(System.in);
        String[] input = new String[20];
        for(int i = 0; i < 20; i++){
            input[i] = myScanner.nextLine();
        }

        int posX = Integer.parseInt(myScanner.nextLine() );
        int posY = Integer.parseInt(myScanner.nextLine() );

        boolean[][] maze = new boolean[20][20];
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 20; j++) {
                maze[i][j] = input[j].charAt(i) != 'X';
            }
        }
        Brain myBrain = new Brain();

        while(lives > 0) {
            String move = myBrain.getMove(maze[posX][posY-1],maze[posX][posY+1],maze[posX+1][posY],maze[posX-1][posY]);
            if(Objects.equals(move,"north") && maze[posX][posY-1]){
                posY--;
            }else if(Objects.equals(move,"south") && maze[posX][posY+1]){
                posY++;
            }else if(Objects.equals(move,"east") && maze[posX+1][posY]){
                posX++;
            }else if(Objects.equals(move,"west") && maze[posX-1][posY]){
                posX--;
            }
            lives--;
            if(posY % 19 == 0 || posX % 19 == 0){
                System.out.println(posX+","+posY);
                System.exit(0);
            }
        }
        System.out.println("You died in the maze!");
    }
}

class Brain {
    Stack<String> path;
    Vertex[] vertices;
    int x,y,nVerts;
    public Brain() {
        path = new Stack<>();
        vertices = new Vertex[400];
        x = y = nVerts = 0;
        vertices[nVerts++] = new Vertex(x,y);
        path.push("start");
    }
    public String getMove(boolean north, boolean south, boolean east, boolean west) {
        if(north && unvisited(x,y-1) )
            return move("north",x,--y);

        if(south && unvisited(x,y+1) )
            return move("south",x,++y);

        if(east && unvisited(x+1,y) )
            return move("east",++x,y);

        if(west && unvisited(x-1,y) )
            return move("west",--x,y);

        switch(path.pop() ) {
            case "north":
                y++;
                return "south";
            case "south":
                y--;
                return "north";
            case "east":
                x--;
                return "west";
            case "west":
                x++;
                return "east";
            default: return ":(";
        }
    }
    private String move(String dir, int x, int y) {
        path.add(dir);
        vertices[nVerts++] = new Vertex(x,y);
        return dir;
    }
    private boolean unvisited(int x, int y) {
        for(int i = 0; i < nVerts; i++)
            if(vertices[i].x == x && vertices[i].y == y)
                return false;
        return true;
    }
}
class Vertex {
    public final int x,y;
    public Vertex(int x,int y) {
        this.x = x;
        this.y = y;
    }
}
