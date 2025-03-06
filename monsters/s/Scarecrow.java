/**
 * 
 */
package com.dndcombat.monsters.s;

import java.util.Arrays;

import com.dndcombat.actions.InvalidActionException;
import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;

/**
 
 */
public class Scarecrow extends Player {
 
	private static final long serialVersionUID = 1L;

	public Scarecrow() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Scarecrow");
//   update creatures set active_ind = 1 where player_ind = 0 and active_ind = 0 and creature_name = 'Scarecrow'; 
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Scarecrow.webp");

	    str(11).dex(13).con(11).inte(10).wis(10).cha(13);

		// Saving Throws
		strSave(+0).dexSave(+1).conSave(+0).intSave(+0).wisSave(+0).chaSave(+1);
	
		 // Combat Stats
		ac(11).init(+3).hp(27).speed(30).cr(1);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesconstruct);

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionsfrightened);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionscharmed);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
 
        addAction(new Action("Fearsome Claw", RefList.actiontypesmainaction)
                 .addAttack(FearsomeClaw()));
        
        addAction(TerrifyingGlare());
	}

    /**
     * Terrifying Glare ability.
     * Forces a Wisdom saving throw on a target within 30 feet.
     * On a failed save, the target becomes Frightened and Paralyzed until the end of the scarecrow's next turn.
     */
    public Action TerrifyingGlare() {
        Action action = new Action("Terrifying Glare", RefList.actiontypesmainaction);

        // Create a SaveDC object for the Wisdom saving throw
        SaveDC save = new SaveDC();
        save.dc = 11; // DC for the saving throw
        save.saveVsRefId = RefList.savingthrowvswisdom; // Wisdom saving throw
        save.conditionRefId = RefList.conditionsparalyzed;

        // Duration for the Frightened and Paralyzed conditions
        DurationType duration = new DurationType(1); // Duration lasts until the end of the scarecrow's next turn
        duration.whoseTurn = DurationType.WhoseTurn.CasterTurn; // The effect ends on the scarecrow's turn
        duration.whenToEnd = DurationType.WhenToEnd.EndOfTurn; // The effect ends at the end of the turn

        // Create a WithinXFeetEffect to apply the Frightened and Paralyzed conditions
        WithinXFeetEffect w = new WithinXFeetEffect(30, action.getActionName(), Arrays.asList(
            new DamageModel().setFailConditionRefId(RefList.conditionsparalyzed).setFailSaveDuration(duration) // No damage, just conditions
        ), save, 0) {
        	 
			private static final long serialVersionUID = 1L;

			@Override
        	public int evaluateScore(Player me, Action a, Player target, long actionTypeRefId, GameState state) throws InvalidActionException {
				if (target.isParalyzed()) {
					throw new InvalidActionException();
				}
				return 20;
        	}
        };

        w.maxNbrTargets = 1; // Only one target can be affected
        action.setMaxNbrUsesPerBattle(1);

        // Set the spell effect on the action
        action.setMonsterSpell(w);

        return action;
    }
    

	private Attack FearsomeClaw() {
	
        Attack a = new Attack("Fearsome Claw"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(4) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }

}

