/**
 * 
 */
package com.dndcombat.monsters.s;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class Skeleton extends Player {
 
	private static final long serialVersionUID = 1L;

	public Skeleton() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Skeleton");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Skeleton.webp");

	    str(10).dex(16).con(15).inte(6).wis(8).cha(5);

		// Saving Throws
		strSave(+0).dexSave(+3).conSave(+2).intSave(-2).wisSave(-1).chaSave(-3);
	
		 // Combat Stats
		ac(14).init(+3).hp(13).speed(30).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionspoisoned);
		getCreature().getDamageVulnerabilities().add(RefList.damagetypesbludgeoning);

        addAction(new Action("Shortsword", RefList.actiontypesmainaction)
                .addAttack(Shortsword()));
       addAction(new Action("Shortbow", RefList.actiontypesmainaction)
                .addAttack(Shortbow()));

	}


	private Attack Shortsword() {
	
        Attack a = new Attack("Shortsword"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
        
        return a;
    }
    private Attack Shortbow() {
        Attack a = new Attack("Shortbow"); 
        a.setAttackBonus(5) 
         .setAttackRange(80) 
         .setLongRange(320) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(6) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         
        // a.setThrownRange(80, 320); 
        return a;
    }

}

