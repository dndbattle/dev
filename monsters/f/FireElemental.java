/**
 * 
 */
package com.dndcombat.monsters.f;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.FireAura;

/**
 
 */
public class FireElemental extends Player {
 
	private static final long serialVersionUID = 1L;

	public FireElemental() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Fire Elemental");
//    
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Fire Elemental.webp");

	    str(10).dex(17).con(16).inte(6).wis(10).cha(7);

		// Saving Throws
		strSave(+0).dexSave(+3).conSave(+3).intSave(-2).wisSave(+0).chaSave(-2);
	
		 // Combat Stats
		ac(13).init(+3).hp(93).speed(50).cr(5);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypeselemental);

		damageResist(RefList.damagetypesslashing);
		damageResist(RefList.damagetypespiercing);
		damageResist(RefList.damagetypesbludgeoning);
		damageImmune(RefList.damagetypespoison);
		damageImmune(RefList.damagetypesfire);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionspoisoned);
		conditionImmune(RefList.conditionsrestrained);
		conditionImmune(RefList.conditionsparalyzed);
		conditionImmune(RefList.conditionspetrified);
		conditionImmune(RefList.conditionsprone);
	 
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( Burn(), Burn() ));

	    // fire aura
        getCreature().addModifierType(new FireAura(1,10));
        
        addAction(new Action("Burn", RefList.actiontypesmainaction)
                .addAttack(Burn()));
	}


	private Attack Burn() {
	
        Attack a = new Attack("Burn"); 
        a.setAttackBonus(6) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesfire); 
        
        return a;
    }

}

