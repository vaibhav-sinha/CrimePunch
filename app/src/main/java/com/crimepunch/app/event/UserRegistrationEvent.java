package com.crimepunch.app.event;

import com.crimepunch.app.base.BaseEvent;
import com.crimepunch.app.model.User;
import lombok.Data;

/**
 * Created by user-1 on 3/10/15.
 */
@Data
public class UserRegistrationEvent extends BaseEvent {

    private User user;
}
