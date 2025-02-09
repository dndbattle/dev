/**
 * 
 */
package com.dndcombat.monsters.b;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.Parry;

/**
 
 */
public class BanditCaptain extends Player {
 
	private static final long serialVersionUID = 1L;

	public BanditCaptain() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Bandit Captain");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Bandit Captain.webp");

	    str(15).dex(16).con(14).inte(14).wis(11).cha(14);

		// Saving Throws
		strSave(4).dexSave(5).conSave(2).intSave(2).wisSave(2).chaSave(2);
	
		 // Combat Stats
		ac(15).init(3).hp(52).speed(30).cr(2);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

        // Multiattack
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Scimitar(), Scimitar()));
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Pistol(), Pistol()));

        // Actions
        addAction(new Action("Scimitar", RefList.actiontypesmainaction)
                .addAttack(Scimitar()));
        addAction(new Action("Pistol", RefList.actiontypesmainaction)
                .addAttack(Pistol()));
        
        getCreature().addModifierType(new Parry(2));
    }
    

	private Attack Scimitar() {
		
        Attack a = new Attack("Scimitar");
        a.setAttackBonus(5)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(6)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypesslashing);
        
        return a;
    }
	

	private Attack Pistol() {
        Attack a = new Attack("Pistol");
        a.setAttackBonus(5)
        .setAttackRange(30)
        .setLongRange(90)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(1)
         .dieType(10)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypespiercing);
        
        return a;
    }
	
	 
}
