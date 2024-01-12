import java.util.*;
public class BreadthFirstSearch
{
    private static LinkedList<LinkedList<Nodes>> tree = new LinkedList<>(); 
    private static Nodes start = new Nodes(0, 0, 'S');

    public static void main(String[]args)
    {
        String seq = "XSXX,XXXX,XXXX,GXXX";

        String[] data = seq.split(",");
        int row = data.length;
        int column = data[0].length();
        
        LinkedList<Nodes> path = new LinkedList<>();
        char[][] matrix = initializeMatrix(data, row, column);
        Nodes start = new Nodes(0, 0, 'S');

        for(int j = 0; j < row; j++)
        {
            for(int k = 0; k < column; k++)
            {
                if(matrix[j][k] == 'S')
                {
                    start = new Nodes(j, k, 'S' );
                }
            }
        }

        path = BreadthFirstSearch(start, matrix, row, column);

        Collections.reverse(path);
        System.out.println("\nPath: " + path);
    }

    public static char[][] initializeMatrix(String[] data, int row, int col)
    {
        char[][] matrix = new char[row][col];

        int size = data.length;
        int i = 0;

        for(int j = 0; j < row; j++)
        {
            String str = "";
            i++;
            for(int k = 0; k < col; k++)
            {
                if(i <= size)
                {
                    str = data[i - 1];
                    matrix[j][k] = str.charAt(k);
                }
            }
        }

        for(int j = 0; j < row; j++)
        {
            System.out.print("\n");
            for(int k = 0; k < col; k++)
            {
                System.out.print(matrix[j][k]);
            }
        }

        return matrix;
    }

    public static List<Nodes> getChildren(char[][] matrix, Nodes curr, int gridRow, int gridCol)
    {
        List<Nodes> children = new LinkedList<>();
        int row = curr.getRow();
        int col = curr.getCol();

        if((col - 1 >= 0 && col - 1 < gridCol) && (matrix[row][col - 1] == 'X' || matrix[row][col - 1] == 'G'))
        {
            children.add(new Nodes(row, col - 1, 'L'));
        }

        if((row + 1 >= 0 && row + 1 < gridRow) && (matrix[row + 1][col] == 'X' || matrix[row + 1][col] == 'G'))
        {
            children.add(new Nodes(row + 1, col, 'D'));
        }

        if((col + 1 >= 0 && col + 1 < gridCol) && (matrix[row][col + 1] == 'X' || matrix[row][col + 1] == 'G'))
        {
            children.add(new Nodes(row, col + 1, 'R'));
        }

        if((row - 1 >= 0 && row - 1 < gridRow) && (matrix[row - 1][col] == 'X' || matrix[row - 1][col] == 'G'))
        {
            children.add(new Nodes(row - 1, col, 'U'));
        }
        return children;
    }

    public static LinkedList<Nodes> constructPath(LinkedList<LinkedList<Nodes>> tree, Nodes child)
    {
        LinkedList<Nodes> path = new LinkedList<>();
        boolean isFound = false;

        int row = child.getRow();
        int col = child.getCol();

        while(!(row == start.getRow() && col == start.getCol()))
        {
            isFound = false;
            for(int j = 0; j < tree.size(); j++)
            {
                LinkedList list = tree.get(j);

                for(int k = 0; k < list.size(); k++)
                {
                    Nodes tmp =  (Nodes)list.get(k);

                    int rw = tmp.getRow();
                    int cl = tmp.getCol();

                    if(row == rw && col == cl)
                    {
                        child = (Nodes)list.get(0);
                        isFound = true;
                        path.add(tmp);
                        break;
                    }
                }

                if(isFound)
                {
                    break;
                }
            }

            row = child.getRow();
            col = child.getCol();
        }
        path.add(child);

        return path;
    }

    public static LinkedList<Nodes> BreadthFirstSearch(Nodes start, char[][] matrix, int gridRow, int gridCol)
    {
        Queue<Nodes> openList = new LinkedList<Nodes>();
        List<Nodes> visited = new LinkedList<Nodes>();
        List<Nodes> filter = new LinkedList<Nodes>();

        openList.add(start);
        while(!openList.isEmpty())
        {
            Nodes curr = openList.poll();
            int row = curr.getRow();
            int col = curr.getCol();

            LinkedList<Nodes> parentChild = new LinkedList<>();

            if(matrix[row][col] == 'G')
            {
                return constructPath(tree, curr);
            }
            else
            {
                visited.add(curr);
                parentChild.add(curr);

                boolean isPresent = false;

                List<Nodes> childs = getChildren(matrix, curr, gridRow, gridCol);

                for(int k = 0; k < childs.size(); k++)
                {
                    int rows = childs.get(k).getRow();
                    int colm = childs.get(k).getCol();
                    isPresent = false;

                    for(int j = 0; j < visited.size(); j++)
                    {
                        int rw = visited.get(j).getRow();
                        int cl = visited.get(j).getCol();

                        if(rows == rw && colm == cl)
                        {
                            isPresent = true;
                            break;
                        }
                    }

                    if(!isPresent)
                    {
                        if(filter.size() == 0)
                        {
                            filter.add(childs.get(k));
                            openList.add(childs.get(k));
                            parentChild.add(childs.get(k));
                        }
                        else
                        {
                            for(int j = 0; j < filter.size(); j++)
                            {
                                Nodes temp = filter.get(j);

                                int rws = temp.getRow();
                                int cls = temp.getCol();

                                if(rows == rws && colm == cls)
                                {
                                    isPresent = true;
                                    break;
                                }
                            }

                            if(!isPresent)
                            {
                                openList.add(childs.get(k));
                                filter.add(childs.get(k));
                                parentChild.add(childs.get(k));
                            }
                        }
                    }
                }
            }
            tree.add(parentChild);
        }
        return null;
    }
}

