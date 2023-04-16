package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        resolveMaze(
                """
                        __M
                        MM_
                        _M_"""
        );
        resolveMaze(
                """
                        __M
                        M__
                        _M_"""
        );
    }

    public record Position(int x, int y) {
    }

    public static void resolveMaze(String maze) {
        var position = new Position(0, 0);
        var matrixMaze = maze.lines().map(s -> Arrays.stream(s.split("")).toList()).toList();
        var endPosition = new Position(matrixMaze.size() - 1, matrixMaze.get(0).size() - 1);
        List<Position> path = new ArrayList<>();
        path.add(position);
        while (pathDoesNotContainFinish(path, endPosition)) {
            var previousState = path.size();
            explore(path, matrixMaze);
            var newState = path.size();
            if (previousState == newState) {
                return;
            }
        }
    }

    private static void explore(List<Position> path, List<List<String>> matrixMaze) {
        var adjacent = Stream.of(
                getRightPos(List.copyOf(path).get(path.size() - 1), matrixMaze),
                getBottomPos(List.copyOf(path).get(path.size() - 1), matrixMaze),
                getLeftPos(List.copyOf(path).get(path.size() - 1), matrixMaze),
                getTopPos(List.copyOf(path).get(path.size() - 1), matrixMaze)
        ).filter(Objects::nonNull).filter(position -> !path.contains(position)).collect(Collectors.toSet());
        path.addAll(adjacent);
    }

    private static Position getTopPos(Position position, List<List<String>> matrixMaze) {
        if (position.x() - 1 >= 0
                && !matrixMaze.get(position.x() - 1).get(position.y()).equals("M")) {
            return new Position(position.x() - 1, position.y());
        }
        return null;
    }

    private static Position getLeftPos(Position position, List<List<String>> matrixMaze) {
        if (position.y() - 1 >= 0
                && !matrixMaze.get(position.x()).get(position.y() - 1).equals("M")) {
            return new Position(position.x(), position.y() - 1);
        }
        return null;
    }

    private static Position getBottomPos(Position position, List<List<String>> matrixMaze) {
        if (position.x() + 1 <= matrixMaze.size() - 1
                && !matrixMaze.get(position.x() + 1).get(position.y()).equals("M")) {
            Position position1 = new Position(position.x() + 1, position.y());
            System.out.println(position1 + "valid");
            return position1;
        }
        return null;
    }

    private static Position getRightPos(Position position, List<List<String>> matrixMaze) {
        if (position.y() + 1 <= matrixMaze.get(0).size() - 1
                && !matrixMaze.get(position.x()).get(position.y() + 1).equals("M")) {
            Position position1 = new Position(position.x(), position.y() + 1);
            System.out.println(position1 + "valid");
            return position1;
        }
        return null;
    }

    private static boolean pathDoesNotContainFinish(List<Position> path, Position endPosition) {
        return !path.contains(endPosition);
    }
}
