package awap;

import java.util.List;

import com.google.common.base.Optional;
import java.util.Collections;
import java.util.LinkedList;

public class Game
{

    private State state;
    //number: Which player I am
    private Integer number;

    public Optional<Move> updateState(State newState)
    {
        if (newState.getError().isPresent())
        {
            Logger.log(newState.getError().get());
            return Optional.absent();
        }

        if (newState.getMove() != -1)
        {
            return Optional.fromNullable(findMove());
        }

        state = newState;
        if (newState.getNumber().isPresent())
        {
            number = newState.getNumber().get();
        }

        return Optional.absent();
    }

    private Move findMove()
    {
        Logger.log("testing123123123123");
        return null;
        
        int N = state.getDimension();
        List<Block> blocks = state.getBlocks().get(number);
        
        List<Move> moveset = new LinkedList<>();
        for (int x = 0; x < N; x++)
        {
            for (int y = 0; y < N; y++)
            {
                for (int rot = 0; rot < 4; rot++)
                {
                    for (int i = 0; i < blocks.size(); i++)
                    {
                        if (canPlace(blocks.get(i).rotate(rot), new Point(x, y)))
                        {
                            moveset.add(new Move(i, rot, x, y));
                        }
                    }
                }
            }
        }
        
        if(moveset.isEmpty())
            return null;
        
        //Collections.shuffle(moveset);
        Move bestMove = moveset.get(0);
        int max = score(bestMove);
        
        for(Move m : moveset)
        {
            final int score = score(m);
            if(score > max)
            {
                bestMove = m;
                max = score;
                Logger.log(m.toString());
            }
        }
        
        return bestMove;
    }
    
    private int score(Move m)
    {
        final List<Block> myBlocks = state.getBlocks().get(number);
        int score = myBlocks.get(m.getIndex()).getSize();
        score *= coversCoin(m) ? 3 : 1;
        return score;
    }
    
    private boolean coversCoin(Move m)
    {
        final List<Block> myBlocks = state.getBlocks().get(number);
        
        Block b = myBlocks.get(m.getIndex());
        
        b = b.rotate(m.getRotations());
        
        Point start = new Point(m.getX(), m.getY());
        
        for(Point p : b.getOffsets())
        {
            Point actual = p.add(start);
            for(Point q : state.getBonusSquaresPoints())
            {
                if(q.equals(actual))
                    return true;
            }
        }
        
        return false;
    }

    private int getPos(int x, int y)
    {
        return state.getBoard().get(x).get(y);
    }

    private boolean canPlace(Block block, Point p)
    {
        boolean onAbsCorner = false, onRelCorner = false;
        int N = state.getDimension() - 1;

        Point[] corners =
        {
            new Point(0, 0), new Point(N, 0), new Point(N, N),
            new Point(0, N)
        };
        ;
        Point corner = corners[number];

        for (Point offset : block.getOffsets())
        {
            Point q = offset.add(p);
            int x = q.getX(), y = q.getY();

            if (x > N || x < 0 || y < 0 || y > N
                    || getPos(x, y) >= 0
                    || getPos(x, y) == -2
                    || (x > 0 && getPos(x - 1, y) == number)
                    || (y > 0 && getPos(x, y - 1) == number)
                    || (x < N && getPos(x + 1, y) == number)
                    || (y < N && getPos(x, y + 1) == number))
            {
                return false;
            }

            onAbsCorner = onAbsCorner || q.equals(corner);
            onRelCorner = onRelCorner
                    || (x > 0 && y > 0 && getPos(x - 1, y - 1) == number)
                    || (x < N && y > 0 && getPos(x + 1, y - 1) == number)
                    || (x > 0 && y < N && getPos(x - 1, y + 1) == number)
                    || (x < N && y < N && getPos(x + 1, y + 1) == number);
        }

        return !((getPos(corner.getX(), corner.getY()) < 0 && !onAbsCorner) || (!onAbsCorner && !onRelCorner));
    }
}
