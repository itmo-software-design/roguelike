package engine.state

import engine.behaviour.IsAliveBehaviour
import vo.Mob

class HealthyState(override val mob: Mob) : State(mob) {

    init {
        mob.behaviour = IsAliveBehaviour(mob.defaultBehaviour)
    }

    override fun checkHealth() {
        if (mob.healthIsCritical()) {
            mob.state = PanicState(mob)
        }
    }
}
