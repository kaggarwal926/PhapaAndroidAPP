package com.icls.offlinekyc.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

//Here we will write queries to access it from database
/*
This is an interface which acts is an intermediary between the user and the database.
        All the operation to be performed on a table has to be defined here.
        We define the list of operation that we would like to perform on table.
*/

@Dao
public interface DAO_Queries {

    @Insert
    Long insertTaskmembertypemaster(Membertypemaster membertypemaster);

    @Insert
    Long insertTask(OrganizationTypeParam org);

}
