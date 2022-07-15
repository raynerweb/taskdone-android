package br.com.raynerweb.ipl.taskdone.ext

import br.com.raynerweb.ipl.taskdone.repository.local.entity.UserEntity
import br.com.raynerweb.ipl.taskdone.ui.model.User

fun User.toUserEntity(): UserEntity = UserEntity(
    name = this.name,
    email = this.email,
    isLocal = this.isLocal,
    photo = this.photo,
)

fun UserEntity.toUser() =
    User(name = this.name, email = this.email, isLocal = this.isLocal, photo = photo)