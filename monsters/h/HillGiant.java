/**
 * 
 */
package com.dndcombat.monsters.h;

import com.dndcombat.fight.model.DurationType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;

/**
 
 */
public class HillGiant extends Player {
 
	private static final long serialVersionUID = 1L;

	public HillGiant() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Hill Giant");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Hill Giant.webp");

	    str(21).dex(8).con(19).inte(5).wis(9).cha(6);

		// Saving Throws
		strSave(+5).dexSave(-1).conSave(+4).intSave(-3).wisSave(-1).chaSave(-2);
	
		 // Combat Stats
		ac(13).init(+2).hp(105).speed(40).cr(5);

        // Size and Type
	    size(RefList.creaturesizeshuge).type(RefList.creaturetypesgiant);


        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( TreeClub(), TreeClub()));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack( TrashLob(), TrashLob()));


        addAction(new Action("Tree Club", RefList.actiontypesmainaction)
                .addAttack(TreeClub()));
       addAction(new Action("Trash Lob", RefList.actiontypesmainaction)
                .addAttack(TrashLob()));

	}


	private Attack TreeClub() {
	
        Attack a = new Attack("Tree Club"); 
        a.setAttackBonus(8) 
         .setMeleereach(10) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(3) 
         .dieType(8) 
         .bonus(5) 
         .setConditionOnlyAffectsLargeOrSmaller(true)
         .autoApplyCondition(RefList.conditionsprone, new DurationType(10))
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
	
    private Attack TrashLob() {
        Attack a = new Attack("Trash Lob"); 
        a.setAttackBonus(8) 
         .setAttackRange(60) 
         .setLongRange(240) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(10) 
         .bonus(5)
         .autoApplyCondition(RefList.conditionspoisoned, new DurationType(1))
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

