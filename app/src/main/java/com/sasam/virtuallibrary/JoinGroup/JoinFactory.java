package com.sasam.virtuallibrary.JoinGroup;

import android.app.Activity;

public class JoinFactory {
    public Join getJoinType(String joinType, Activity activity){
        if(joinType == null){
            return null;
        }
        if(joinType.equalsIgnoreCase("normal")){
            return new normalMemberJoin(activity);

        } else if(joinType.equalsIgnoreCase("rejoin")){
            return new tryForReJoin(activity);

        }

        return null;
    }
}
