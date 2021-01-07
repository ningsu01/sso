package com.sso.core.model;

import lombok.*;

import javax.persistence.*;

/**
 * Created by nings on 2020/12/6.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_user")
public class UserInfo {

    /**用户id**/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
    /**名称**/
    @NonNull
    @Column
    private String username;
    /**密码**/
    @NonNull
    @Column
    private String password;

    @Column
    private Integer deleted;

}
