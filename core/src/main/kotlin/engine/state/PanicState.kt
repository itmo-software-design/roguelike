package engine.state

import engine.behaviour.FearfulBehaviour
import engine.behaviour.IsAliveBehaviour
import vo.Mob

class PanicState(override val mob: Mob) : State(mob) {

    init {
        mob.behaviour = IsAliveBehaviour(FearfulBehaviour(mob.defaultBehaviour))
    }

    override fun checkHealth() {
        if (!mob.healthIsCritical()) {
            mob.state = HealthyState(mob)
        }
    }
}
