package engine.state

import vo.Mob

abstract class State(protected open val mob: Mob) {
    abstract fun checkHealth()
}
