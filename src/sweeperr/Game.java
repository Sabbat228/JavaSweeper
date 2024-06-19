package sweeperr;

public class Game
{
    private Bomb bomb;
    private Flag flag;
    private GameState state;

    public GameState getState()
    {
        return state;
    }
    public Game (int cols, int rows, int bombs)
    {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();
    }
    public void start()
    {
        state = GameState.PLAYED;
        bomb.start();
        flag.start();
    }
    public Box getBox(Coord coord)
    {
        if (flag.get(coord) == Box.OPENED)
            return bomb.get(coord);
        else
            return flag.get(coord);
    }

    public void pressLeftButton(Coord coord)
    {
        if (gameOver()) return;
        openBox(coord);
        checkWinner();
    }
    private void checkWinner()
    {
        if (state == GameState.PLAYED)
            if (flag.getCountOfClosedBoxes() == bomb.getTotalBombs())
                state = GameState.WINNER;
    }

    private void openBox(Coord coord)
    {
        switch (flag.get(coord))
        {
            case OPENED : setCountOfClosedBoxesAroundNumber(coord); return;
            case FLAGED : return;
            case CLOSED :
                switch (bomb.get(coord))
                {
                    case ZERO : openBoxArpound(coord); return;
                    case BOMB : openBombs(coord); return;
                    default : flag.setOpenedToBox(coord); return;
                }
        }
    }

    private void setCountOfClosedBoxesAroundNumber(Coord coord)
    {
        if (bomb.get (coord)!= Box.BOMB)
        if(flag.getCountOfFlagedBoxesAround(coord) == bomb.get(coord).getNumber())
            for (Coord around : Ranges.getAllCooords())
                if (flag.get(around) == Box.CLOSED)
                    openBox(coord);

    }

    private void openBombs(Coord bombed)
    {
        state = GameState.BOMBED;
        flag.setBombedToBox(bombed);
        for (Coord coord : Ranges.getAllCooords())
            if(bomb.get(coord) == Box.BOMB)
                flag.setOpenedToClosedBombBox(coord);
            else
                flag.setNoBombedToFlageSafeBox(coord);
    }

    private void openBoxArpound(Coord coord)
    {
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordsAround(coord))
            openBox(around);
    }

    public void pressRightButton(Coord coord)
    {
        if (gameOver()) return;
        flag.togglerFlagedToBox(coord);
    }

    private boolean gameOver()
    {
        if (state == GameState.PLAYED)
            return false;
        start();
        return true;
    }
}
