/**
 * 
 */
package com.dndcombat.monsters.y;

import java.util.Arrays;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;

/**
 
 */
public class Yeti extends Player {
 
	private static final long serialVersionUID = 1L;

	public Yeti() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Yeti");
//    
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Yeti.webp");

	    str(18).dex(13).con(16).inte(8).wis(12).cha(7);

		// Saving Throws
		strSave(+4).dexSave(+1).conSave(+3).intSave(-1).wisSave(+1).chaSave(-2);
	
		 // Combat Stats
		ac(12).init(+1).hp(51).speed(40).cr(3);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesmonstrosity);

		damageImmune(RefList.damagetypescold);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Claw(), Claw(), chillingGaze() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(IceThrow(), IceThrow(), chillingGaze() ));


        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));
        addAction(new Action("Ice Throw", RefList.actiontypesmainaction)
                .addAttack(IceThrow()));

	}


    private Attack chillingGaze() {
		Attack a =new Attack();
		a.setAttackName("Chilling Gaze");
		SaveDC save = new SaveDC();
		save.dc = 13;
		save.saveVsRefId = RefList.savingthrowvsconstitution;
		WithinXFeetEffect w = new WithinXFeetEffect(30, a.getAttackName(), Arrays.asList(
				new DamageModel().nbrDice(2).dieType(4).saveOrDamage(save)
				.setFailConditionRefId(RefList.conditionsparalyzed).setFailSaveDuration(new DurationType(1))),
				null, 1);
		w.maxNbrTargets = 1;
		a.setSpell(w);
		return a;
	}
    
	private Attack Claw() {
	
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing); 
        
        return a;
    }
    private Attack IceThrow() {
        Attack a = new Attack("Ice Throw"); 
        a.setAttackBonus(6) 
         .setAttackRange(30) 
         .setLongRange(120) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(4) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
         
        // a.setThrownRange(30, 120); 
        return a;
    }

}

