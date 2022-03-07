package model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import model.base.BaseModel;
import util.BaseData;
import util.enums.BotState;
import util.enums.Role;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
@ToString(callSuper = true)
public class User extends BaseModel {
    String name;
    String phoneNumber;
    String username;
    String chatId;
    BotState state;
    Role role;
    boolean isAdmin=false;
}
