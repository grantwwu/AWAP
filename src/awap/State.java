package awap;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.ArrayList;

public class State
{
    private Optional<String> error = Optional.absent();
    //IGNORE THIS FIELD
    private Optional<Integer> number = Optional.absent(); 
    /* -2
     * -1 means empty
     * 0-3 means occupied by said player */
    private List<List<Integer>> board;
    // Blocks[i] is the list of blocks for player i
    private List<List<Block>> blocks;
    private int dimension; //Equals 20
    private int turn; //Means whose turn it is
    private int move = -1; //1: expecting a move, otherwise: not expecting move.
    private String url; //Why is this in state?
    private List<List<Integer>> bonusSquares; //List of coordinates

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Optional<String> getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = Optional.fromNullable(error);
    }

    public Optional<Integer> getNumber()
    {
        return number;
    }

    public void setNumber(int playerID)
    {
        this.number = Optional.fromNullable(playerID);
    }

    public List<List<Integer>> getBoard()
    {
        return board;
    }

    @SuppressWarnings("unchecked")
    public void setBoard(Map<String, Object> board)
    {
        this.board = (List<List<Integer>>) board.get("grid");
        this.bonusSquares = (List<List<Integer>>) board.get("bonus_squares");
        this.setDimension((int) board.get("dimension"));
    }

    public int getMove()
    {
        return move;
    }

    public void setMove(int move)
    {
        this.move = move;
    }

    public int getDimension()
    {
        return dimension;
    }

    public void setDimension(int dimension)
    {
        this.dimension = dimension;
    }

    public int getTurn()
    {
        return turn;
    }

    public void setTurn(int turn)
    {
        this.turn = turn;
    }

    public List<List<Block>> getBlocks()
    {
        return blocks;
    }

    public void setBlocks(List<List<List<Map<String, Integer>>>> blocks)
    {
        List<List<Block>> blockList = Lists.newArrayList();
        for (List<List<Map<String, Integer>>> player : blocks)
        {
            List<Block> playerList = Lists.newArrayList();
            for (List<Map<String, Integer>> block : player)
            {
                playerList.add(new Block(block));
            }
            blockList.add(playerList);
        }

        this.blocks = blockList;
    }

    public List<List<Integer>> getBonusSquares()
    {
        return bonusSquares;
    }
    
    public List<Point> getBonusSquaresPoints()
    {
        List<Point> ret = new ArrayList<>(bonusSquares.size());
        for(List<Integer> tuple : bonusSquares)
        {
            ret.add(new Point(tuple.get(0), tuple.get(1)));
        }
        return ret;
    }

    public void setPlayers(List<String> players)
    {
    }
}
