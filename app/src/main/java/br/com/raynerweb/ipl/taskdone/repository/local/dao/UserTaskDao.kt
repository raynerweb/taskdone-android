package br.com.raynerweb.ipl.taskdone.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserTaskEntity

@Dao
interface UserTaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(join: List<UserTaskEntity>)

}

