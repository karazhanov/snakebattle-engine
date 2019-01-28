package com.codenjoy.dojo.snakebattle.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.model.Elements;

import java.util.List;

import static com.codenjoy.dojo.snakebattle.model.Elements.*;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@see AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Elements> {

    private boolean[][] goodCells;
    private boolean[][] badCells;

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, WALL, START_FLOOR, ENEMY_HEAD_SLEEP, ENEMY_TAIL_INACTIVE, TAIL_INACTIVE, STONE);
    }

//    public

    public Direction canEatDirection(Direction curDirection) {
        Point me = getMe();
        int y = me.getY();
        int x = me.getX();
        if (curDirection != Direction.LEFT && isAt(x + 1, y, APPLE, FLYING_PILL, FURY_PILL, GOLD)) {
            return Direction.RIGHT;
        }
        if (curDirection != Direction.RIGHT && isAt(x - 1, y, APPLE, FLYING_PILL, FURY_PILL, GOLD)) {
            return Direction.LEFT;
        }
        if (curDirection != Direction.UP && isAt(x, y - 1, APPLE, FLYING_PILL, FURY_PILL, GOLD)) {
            return Direction.DOWN;
        }
        if (curDirection != Direction.DOWN && isAt(x, y + 1, APPLE, FLYING_PILL, FURY_PILL, GOLD)) {
            return Direction.UP;
        }

        if(curDirection == Direction.RIGHT && !isBarrierAt(x + 1, y)) {
            return Direction.RIGHT;
        }
        if(curDirection == Direction.LEFT && !isBarrierAt(x - 1, y)) {
            return Direction.LEFT;
        }
        if(curDirection == Direction.DOWN && !isBarrierAt(x, y - 1)) {
            return Direction.DOWN;
        }
        if(curDirection == Direction.UP && !isBarrierAt(x, y + 1)) {
            return Direction.UP;
        }
        if(curDirection == Direction.STOP) {
            return Direction.RIGHT;
        }
        return curDirection.clockwise();
    }


    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    public Point getMe() {
        return getMyHead().get(0);
    }

    public boolean isGameOver() {
        return getMyHead().isEmpty();
    }

    private List<Point> getMyHead() {
        return get(HEAD_DOWN, HEAD_LEFT, HEAD_RIGHT, HEAD_UP, HEAD_SLEEP, HEAD_EVIL, HEAD_FLY);
    }

    public boolean isStoneAt(int x, int y) {
        return isAt(x, y, STONE);
    }

    public void initAI() {
        goodCells = new boolean[size][size];
        badCells = new boolean[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                goodCells[x][y] = isAt(x, y, APPLE, FLYING_PILL, FURY_PILL, GOLD);
                badCells[x][y] = isBarrierAt(x, y);
            }
        }
        List<Elements> near = this.getAllAt(getMe());
        near.forEach(System.out::print);
    }
}
