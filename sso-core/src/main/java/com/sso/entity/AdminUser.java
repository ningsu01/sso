package com.sso.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by nings on 2020/12/5.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 5151154987990786511L;

    /**用户id**/
    private String userid;
    /**用户名称**/
    private String username;

    private Map<String, String> plugininfo;
    /**版本**/
    private String version;
    /**超时时间**/
    private int expireMinite;
    /**刷新时间**/
    private long expireFreshTime;


}
