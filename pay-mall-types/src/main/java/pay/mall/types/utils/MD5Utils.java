package pay.mall.types.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * MD5加密工具
 */
public class MD5Utils {

    /**
     * 对密码加密    返回16位的MD5码
     * @param plainText 加密文本
     * @return  16位的MD5码
     */
    public static String MD5(String plainText) {
        String ans = plainText;
        for(int i = 1 ; i <= 15 ; i++)
        {
            assert ans != null;
            ans = lock(ans);
        }
        return encode(ans);
    }

    private static String lock(String plainText)
    {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            return new BigInteger(1,  md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *  校验MD5码
     * @param text 要校验的字符串
     * @param md5   md5值
     * @return 校验结果
     */
    public static boolean reLock(String text, String md5) {
        return md5.equals(MD5(text));
    }

    public static String encode(String context)
    {
        StringBuilder ans = new StringBuilder();
        Map<Character , Character> upper = PersonalLock.UpperLock();
        Map<Character , Character> lower = PersonalLock.LowerLock();
        Map<Character , Character> numer = PersonalLock.NumLock();
        char[] list = context.toCharArray();

        for (char c : list) {
            if ('a' <= c && c <= 'z') ans.append(lower.get(c));
            else if ('A' <= c && c <= 'Z') ans.append(upper.get(c));
            else if ('0' <= c && c <= '9') ans.append(numer.get(c));
            else ans.append(c);
        }

        return ans.toString();
    }
}
