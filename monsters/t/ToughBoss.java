/**
 * 
 */
package com.dndcombat.monsters.t;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.PackTactics;

/**
 
 */
public class ToughBoss extends Player {
 
	private static final long serialVersionUID = 1L;

	public ToughBoss() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Tough Boss");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Tough Boss.webp");

	    str(17).dex(14).con(16).inte(11).wis(10).cha(11);

		// Saving Throws
		strSave(+5).dexSave(+2).conSave(+5).intSave(+0).wisSave(+0).chaSave(+2);
	
		 // Combat Stats
		ac(16).init(+2).hp(82).speed(30).cr(4);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypeshumanoid);

	    //TODO push
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Warhammer(), Warhammer() ));

        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(HeavyCrossbow(), HeavyCrossbow() ));

        addAction(new Action("Warhammer", RefList.actiontypesmainaction)
                .addAttack(Warhammer()));
        addAction(new Action("Heavy Crossbow", RefList.actiontypesmainaction)
                .addAttack(HeavyCrossbow()));

        getCreature().addModifierType(new PackTactics());
	}


	private Attack Warhammer() {
	
        Attack a = new Attack("Warhammer"); 
        a.setAttackBonus(5) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(8) 
         .bonus(3) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }
    private Attack HeavyCrossbow() {
        Attack a = new Attack("Heavy Crossbow"); 
        a.setAttackBonus(4) 
         .setAttackRange(100) 
         .setLongRange(400) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(2) 
         .dieType(10) 
         .bonus(2) 
         .damageTypeRefId(RefList.damagetypespiercing); 
         
        // a.setThrownRange(100, 400); 
        return a;
    }

}

