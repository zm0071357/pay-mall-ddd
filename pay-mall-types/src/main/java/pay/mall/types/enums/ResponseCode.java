package pay.mall.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    LACK_ACCOUNT("0003", "缺少账号"),
    LACK_PASSWORD("0004", "缺少密码"),
    ACCOUNT_OR_PASSWORD_ERROR("0005", "账号或密码错误"),
    ;

    private String code;
    private String info;

}
