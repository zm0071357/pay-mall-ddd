package pay.mall.types.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 加密
 */
public class PersonalLock {

    /**
     * 对数字加密
     * @return
     */
    public static Map<Character , Character> NumLock(){
        Map<Character , Character> upperLock = new HashMap<>();

        upperLock.put('0' , '1');
        upperLock.put('1' , '3');
        upperLock.put('2' , '5');
        upperLock.put('3' , '2');
        upperLock.put('4' , '9');
        upperLock.put('5' , '0');
        upperLock.put('6' , '7');
        upperLock.put('7' , '4');
        upperLock.put('8' , '6');
        upperLock.put('9' , '8');

        return upperLock;
    }

    /**
     * 对大写字母加密
     * @return
     */
    public static Map<Character , Character> UpperLock(){
        Map<Character , Character> upperLock = new HashMap<>();

        upperLock.put('A' , 'Q');
        upperLock.put('B' , 'W');
        upperLock.put('C' , 'E');
        upperLock.put('D' , 'R');
        upperLock.put('E' , 'T');
        upperLock.put('F' , 'Y');
        upperLock.put('G' , 'U');
        upperLock.put('H' , 'I');
        upperLock.put('I' , 'O');
        upperLock.put('J' , 'P');
        upperLock.put('K' , 'A');
        upperLock.put('L' , 'S');
        upperLock.put('M' , 'D');
        upperLock.put('N' , 'F');
        upperLock.put('O' , 'G');
        upperLock.put('P' , 'H');
        upperLock.put('Q' , 'J');
        upperLock.put('R' , 'K');
        upperLock.put('S' , 'L');
        upperLock.put('T' , 'Z');
        upperLock.put('U' , 'X');
        upperLock.put('V' , 'C');
        upperLock.put('W' , 'V');
        upperLock.put('X' , 'B');
        upperLock.put('Y' , 'N');
        upperLock.put('Z' , 'M');


        return upperLock;
    }

    /**
     * 对小写字母加密
     * @return
     */
    public static Map<Character , Character> LowerLock(){
        Map<Character , Character> upperLock = new HashMap<>();

        upperLock.put('a' , 'm');
        upperLock.put('b' , 'n');
        upperLock.put('c' , 'b');
        upperLock.put('d' , 'v');
        upperLock.put('e' , 'c');
        upperLock.put('f' , 'x');
        upperLock.put('g' , 'z');
        upperLock.put('h' , 'a');
        upperLock.put('i' , 'd');
        upperLock.put('j' , 's');
        upperLock.put('k' , 'f');
        upperLock.put('l' , 'g');
        upperLock.put('m' , 'h');
        upperLock.put('n' , 'j');
        upperLock.put('o' , 'k');
        upperLock.put('p' , 'l');
        upperLock.put('q' , 'p');
        upperLock.put('r' , 'o');
        upperLock.put('s' , 'i');
        upperLock.put('t' , 'u');
        upperLock.put('u' , 'y');
        upperLock.put('v' , 't');
        upperLock.put('w' , 'r');
        upperLock.put('x' , 'e');
        upperLock.put('y' , 'q');
        upperLock.put('z' , 'w');


        return upperLock;
    }

    /**
     * 对大写字母解密
     * @return
     */
    public static Map<Character , Character> UnUpperLock() {
        Map<Character, Character> upperLock = new HashMap<>();

        upperLock.put('Q' , 'A');
        upperLock.put('W' , 'B');
        upperLock.put('E' , 'C');
        upperLock.put('R' , 'D');
        upperLock.put('T' , 'E');
        upperLock.put('Y' , 'F');
        upperLock.put('U' , 'G');
        upperLock.put('I' , 'H');
        upperLock.put('O' , 'I');
        upperLock.put('P' , 'J');
        upperLock.put('A' , 'K');
        upperLock.put('S' , 'L');
        upperLock.put('D' , 'M');
        upperLock.put('F' , 'N');
        upperLock.put('G' , 'O');
        upperLock.put('H' , 'P');
        upperLock.put('J' , 'Q');
        upperLock.put('K' , 'R');
        upperLock.put('L' , 'S');
        upperLock.put('Z' , 'T');
        upperLock.put('X' , 'U');
        upperLock.put('C' , 'V');
        upperLock.put('V' , 'W');
        upperLock.put('B' , 'X');
        upperLock.put('N' , 'Y');
        upperLock.put('M' , 'Z');

        return upperLock;
    }

    /**
     * 对小写字母解密
     * @return
     */
    public static Map<Character , Character> UnLowerLock(){
        Map<Character , Character> upperLock = new HashMap<>();

        upperLock.put('m' , 'a' );
        upperLock.put('n' , 'b' );
        upperLock.put('b' , 'c' );
        upperLock.put('v' , 'd' );
        upperLock.put('c' , 'e' );
        upperLock.put('x' , 'f' );
        upperLock.put('z' , 'g' );
        upperLock.put('a' , 'h' );
        upperLock.put('d' , 'i' );
        upperLock.put('s' , 'j' );
        upperLock.put('f' , 'k' );
        upperLock.put('g' , 'l' );
        upperLock.put('h' , 'm' );
        upperLock.put('j' , 'n' );
        upperLock.put('k' , 'o' );
        upperLock.put('l' , 'p' );
        upperLock.put('p' , 'q' );
        upperLock.put('o' , 'r' );
        upperLock.put('i' , 's' );
        upperLock.put('u' , 't' );
        upperLock.put('y' , 'u' );
        upperLock.put('t' , 'v' );
        upperLock.put('r' , 'w' );
        upperLock.put('e' , 'x' );
        upperLock.put('q' , 'y' );
        upperLock.put('w' , 'z' );


        return upperLock;
    }

    /**
     * 对数字解密
     * @return
     */
    public static Map<Character , Character> NumUnLock(){
        Map<Character , Character> upperLock = new HashMap<>();

        upperLock.put('1' , '0');
        upperLock.put('3' , '1');
        upperLock.put('5' , '2');
        upperLock.put('2' , '3');
        upperLock.put('9' , '4');
        upperLock.put('0' , '5');
        upperLock.put('7' , '6');
        upperLock.put('4' , '7');
        upperLock.put('6' , '8');
        upperLock.put('8' , '9');

        return upperLock;
    }
}
