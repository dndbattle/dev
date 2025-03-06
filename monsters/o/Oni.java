/**
 * 
 */
package com.dndcombat.monsters.o;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.Regeneration;

/**
 
 */
public class Oni extends Player {
 
	private static final long serialVersionUID = 1L;

	public Oni() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Oni");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Oni.webp");

	    str(19).dex(11).con(16).inte(14).wis(12).cha(15);

		// Saving Throws
		strSave(+4).dexSave(+3).conSave(+6).intSave(+2).wisSave(+4).chaSave(+5);
	
		 // Combat Stats
		ac(17).init(+0).hp(119).speed(30).flySpeed(30).cr(7);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesfiend);


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Claw(), Claw()));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( NightmareRay(), NightmareRay()));

        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));
        addAction(new Action("Nightmare Ray", RefList.actiontypesmainaction)
                .addAttack(NightmareRay()));

        getCreature().addModifierType(new Regeneration(10));
	}


	private Attack Claw() {
	
        Attack a = new Attack("Claw"); 
        a.setAttackBonus(7) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(12) 
         .bonus(4) 
         .damageTypeRefId(RefList.damagetypesslashing);
        a.addDamage().nbrDice(2).dieType(8).damageTypeRefId(RefList.damagetypesnecrotic);
        
        return a;
    }
    private Attack NightmareRay() {
        Attack a = new Attack("Nightmare Ray"); 
        a.setAttackBonus(5) 
         .setAttackRange(60) 
         .setLongRange(0) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(2) 
         .autoApplyCondition(RefList.conditionsfrightened, new DurationType(1))
         .damageTypeRefId(RefList.damagetypespsychic); 
         
         
        return a;
    }

}

