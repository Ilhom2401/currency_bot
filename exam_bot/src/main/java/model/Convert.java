package model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import model.base.BaseModel;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
@ToString(callSuper = true)
public class Convert extends BaseModel {
    String chatId;
    String username;
    String to;
    String from;
    String amount;

}
