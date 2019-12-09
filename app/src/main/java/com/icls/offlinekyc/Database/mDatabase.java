package com.icls.offlinekyc.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/*
This is an abstract class where you define all the entities that means all the tables
that you want to create for that database. We define the list of operation that we would like to perform on table.
*/

@Database(entities = {Membertypemaster.class, OrganizationTypeParam.class}, version = 4, exportSchema = false)
public abstract class mDatabase extends RoomDatabase {

    public abstract DAO_Queries dao_queries();


}
