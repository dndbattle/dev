/**
 * 
 */
package com.dndcombat.monsters.z;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.UndeadFortitude;

/**
 
 */
public class Zombie extends Player {
 
	private static final long serialVersionUID = 1L;

	public Zombie() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Zombie");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Zombie.webp");

	    str(13).dex(6).con(16).inte(3).wis(6).cha(5);

		// Saving Throws
		strSave(+1).dexSave(-2).conSave(+3).intSave(-4).wisSave(+0).chaSave(-3);
	
		 // Combat Stats
		ac(8).init(-2).hp(15).speed(20).cr(0.25);

        // Size and Type
	    size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

		damageImmune(RefList.damagetypespoison);
		conditionImmune(RefList.conditionsexhaustion);
		conditionImmune(RefList.conditionspoisoned);

        addAction(new Action("Slam", RefList.actiontypesmainaction)
                .addAttack(Slam()));
        
        getCreature().addModifierType(new UndeadFortitude());
	}


	private Attack Slam() {
	
        Attack a = new Attack("Slam"); 
        a.setAttackBonus(3) 
         .setMeleereach(5) 
         .setRollToHitInd(1) 
         .getFirstDamage() 
         .nbrDice(1) 
         .dieType(8) 
         .bonus(1) 
         .damageTypeRefId(RefList.damagetypesbludgeoning); 
        
        return a;
    }

}

