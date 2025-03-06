/**
 * 
 */
package com.dndcombat.monsters.m;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.DurationType.WhenToEnd;
import com.dndcombat.fight.model.DurationType.WhoseTurn;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.BreathWeapon;
import com.dndcombat.monster.actions.ConeBreath;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import com.dndcombat.monster.traits.MagicResistance;
import com.dndcombat.spells.wizard.DominateMonster;

/**
 
 */
public class MindFlayer extends Player {
 
	private static final long serialVersionUID = 1L;

	public MindFlayer() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Mind Flayer");

	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Mind Flayer.webp");

	    str(11).dex(12).con(12).inte(19).wis(17).cha(17);

		// Saving Throws
		strSave(+0).dexSave(+4).conSave(+1).intSave(+7).wisSave(+6).chaSave(+6);
	
		 // Combat Stats
		ac(15).init(+4).hp(99).speed(30).flySpeed(15).cr(7);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesaberration);

		damageResist(RefList.damagetypespsychic);
		getCreature().addModifierType(new MagicResistance());
 
		setMonsterSpellDC(15); 
	 
        addAction(new Action("Tentacles", RefList.actiontypesmainaction)
                .addAttack(Tentacles()));
        
        addAction(ExtractBrain());
        
	    // Actions
	    addAction(new Action("Mind Blast", RefList.actiontypesmainaction)
	            .addAttack(mindBlast()));
	     
		
		addAction(dominateMonster());

	}

    private Action dominateMonster() {

		Action a = new Action("Dominate Monster", RefList.actiontypesmainaction);

		a.setMonsterSpell(new DominateMonster());

		return a;

	}
    

    private Action ExtractBrain() {
    	Action action = new Action("Extract Brain", RefList.actiontypesmainaction);
    	action.setMustBeGrappledByMe(true);
        
        SaveDC save = new SaveDC();
        save.dc = 15;
        save.saveVsRefId = RefList.savingthrowvsconstitution;
        
        WithinXFeetEffect w = new WithinXFeetEffect(5, action.getActionName(),
        		Arrays.asList(new DamageModel().nbrDice(10).dieType(10).killOnZeroHPAndFail().damageTypeRefId(RefList.damagetypespiercing)), 
        		save, 0);
        w.attackSettings.halfDamage = true;
        w.maxNbrTargets = 1;
        
        action.setMonsterSpell(w);
        return action;
    }

	/**
	 * Creates the Mind Blast attack.
	 *
	 * @return The Attack object representing Mind Blast.
	 */
	private Attack mindBlast() {
		
		 // Set the condition for stunned on a failed save
	    DurationType stunDuration = new DurationType(1);
	    stunDuration.whoseTurn = WhoseTurn.TargetTurn;
	    stunDuration.whenToEnd = WhenToEnd.EndOfTurn;
 
	    // Build the breath weapon
	    BreathWeapon bw = BreathWeapon.builder()
	            .actionTypeRefId(RefList.actiontypesmainaction)
	            .breathName("Mind Blast")
	            .recharge(5) // Recharge on a 5-6
	            .shape("Cone")
	            .coneOrSphereSizeFeet(60) // 60-foot cone
	            .damage(Arrays.asList(
	                    new DamageModel()
	                            .nbrDice(6)
	                            .dieType(8)
	                            .bonus(4)
	                            .damageTypeRefId(RefList.damagetypespsychic)
	                            .setFailConditionRefId(RefList.conditionsstunned)
	                            .setFailSaveDuration(stunDuration)
	            ))
	            .saveVsRefId(RefList.savingthrowvsintelligence) // Intelligence saving throw
	            .saveDC(15) // DC 15
	            .build();
	 
	    // Wrap it in an Attack
	    Attack a = new Attack("Mind Blast");
	    a.setSpell(new ConeBreath(bw));
	    return a;
	}
 

	private Attack Tentacles() {
	
        Attack a = new Attack("Tentacles"); 
        a.setAttackBonus(7) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .setGrappleOnHit(true)
         .setGrappleDc(14)
         .setGrappleMediumOrSmaller(true)
         .setGrappleIsStunned(true)
         .getFirstDamage()
         .nbrDice(4) 
         .dieType(8) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypespsychic); 
        
        return a;
    }

}

