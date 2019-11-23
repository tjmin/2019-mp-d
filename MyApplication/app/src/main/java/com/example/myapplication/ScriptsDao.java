//작성자: 이원구
// for database(1 of 3)
// Dao (data object access) to access Scripts (delete, insert..)

package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Script;

import java.util.List;

@Dao
public interface ScriptsDao {

   // if the Script exist replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScript(Script script);

    @Delete
    void deleteScript(Script script);

    @Update
    void updateScript(Script script);

    // list All Scripts From Database
    @Query("SELECT * FROM scripts")
    List<Script> getScripts();


    @Query("SELECT * FROM scripts WHERE id = :scriptId") // get Script by id
    Script getScriptById(int scriptId);


    @Query("DELETE FROM scripts WHERE id = :scriptId")
    void deleteScriptById(int scriptId);

}
