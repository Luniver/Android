package hrst.sczd.agreement.sczd;

/**
 * 433 无线模块通信
 * Created by Sophimp on 2019/1/9.
 */

public interface Cmd433 {

    /**
     * 频点设置
     */
    short PDA_TO_MCU_REQUEST_FREQUENCY_SETTING = 0X21;

    short MCU_TO_PDA_FREQUENCY_SETTING_ACK = 0X21 + 0x80;

    /**
     * 系统自检
     */
    short PDA_TO_MCU_REQUEST_SYSTEM_SELF_CHECK = 0x22;
    short MCU_TO_PDA_SYSTEM_SELF_CHECK_ACK = 0x22 + 0x80;

    /**
     * mcu 固件升级
     */
    short PDA_TO_MCU_REQUEST_FIRM_UPGRADE = 0x23;
    short MCU_TO_PDA_FIRM_UPGRADE_ACK= 0x23 + 0x80;

    /**
     * 版本查询
     */
    short PDA_TO_MCU_REQEUST_QUERY_VERSION = 0x24;
    short MCU_TO_PDA_QUERY_VERSION_ACK = 0x24 + 0x80;

    /**
     * flash版本查询
     */
    short PDA_TO_MCU_REQEUST_QUERY_FLASH_VERSION = 0x26;
    short MCU_TO_PDA_QUERY_FLASH_VERSION_ACK = 0x26 + 0x80;
}
