/**
 * 
 */
package com.dndcombat.monsters.b;

import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.RefList;
import com.dndcombat.monster.traits.CorrosiveForm;

/**
  TODO SPLIT
  Split. Trigger: While the pudding is Large or Medium and has 10+ Hit Points, it becomes Bloodied or is subjected to Lightning or Slashing damage. Response: The pudding splits into two new Black Puddings. Each new pudding is one size smaller than the original pudding and acts on its Initiative. The original pudding’s Hit Points are divided evenly between the new puddings (round down).
  
 */
public class BlackPudding extends Player {
 
	private static final long serialVersionUID = 1L;

	public BlackPudding() {
		super(new Creature());
		create();
    }

    /**
     */
    public void create() {
    	getCreature().setCreatureName("Black Pudding");
	    setPlayersSide(false);
	    getCreature().setImageUrl("/assets/monsters/MM/Black Pudding.webp");

	    str(16).dex(5).con(16).inte(1).wis(6).cha(1);

		// Saving Throws
		strSave(+3).dexSave(-3).conSave(+3).intSave(-5).wisSave(-2).chaSave(-5);
	
		 // Combat Stats
		ac(7).init(-3).hp(68).speed(20).cr(4);

        // Size and Type
	    size(RefList.creaturesizeslarge).type(RefList.creaturetypesooze);
	    
	    damageImmune(RefList.damagetypesacid, RefList.damagetypescold, RefList.damagetypeslightning, RefList.damagetypesslashing);
	    conditionImmune(RefList.conditionscharmed, RefList.conditionsdeafened, RefList.conditionsexhaustion, RefList.conditionsfrightened, RefList.conditionsgrappled,
	    		RefList.conditionsprone, RefList.conditionsrestrained);
	    
        addAction(new Action("Dissolving Pseudopod", RefList.actiontypesmainaction)
                .addAttack(DissolvingPseudopod()));
        
        getCreature().addModifierType(new CorrosiveForm());
    }
    

	private Attack DissolvingPseudopod() {
		
        Attack a = new Attack("Dissolving Pseudopod");
        a.setAttackBonus(5)
         .setMeleereach(10)
         .setRollToHitInd(1)
         .getFirstDamage()
         .nbrDice(4)
         .dieType(6)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypesacid);
        
        return a;
    }
	
    
}

