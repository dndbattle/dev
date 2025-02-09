/**
 * 
 */
package com.dndcombat.monsters.a;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.Player;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.DamageType;
import com.dndcombat.model.DieType;
import com.dndcombat.model.NbrOfDice;
import com.dndcombat.model.RefList;
import com.dndcombat.spells.basic.SpellSlot;
import com.dndcombat.util.Converter;

/**
 * 
 */

public class AarakocraAeromancer extends Player {

 
	private static final long serialVersionUID = 1L;

	public AarakocraAeromancer() {
		super(new Creature());
		create();
	}
	
	 public void create() {
	        getCreature().setCreatureName("Aarakocra Aeromancer");
	        setPlayersSide(false);
	        getCreature().setImageUrl("/assets/monsters/MM/Aarakocra.webp");

	        // Ability Scores
	        str(10).dex(16).con(12).inte(13).wis(17).cha(12);

	        // Saving Throws
	        strSave(0).dexSave(5).conSave(1).intSave(1).wisSave(5).chaSave(1);

	        // Combat Stats
	        ac(16).init(3).hp(66).speed(20).flySpeed(50).cr(4);

	        // Size and Type
	        size(RefList.creaturesizesmedium).type(RefList.creaturetypeselemental);
  
	        // Actions
	        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
	                .addAttack(WindStaff(), WindStaff()));
	        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
	                .addAttack(WindStaffRanged(), WindStaffRanged()));
 
	        // Actions
	        addAction(new Action("Wind Staff", RefList.actiontypesmainaction)
	                .addAttack(WindStaff()));
	        addAction(new Action("Wind Staff", RefList.actiontypesmainaction)
	                .addAttack(WindStaffRanged()));
	        
	        setMonsterSpellDC(13);
	        addAction(LightningBolt());
	        // delay moving closer
	        setMoveCloserAfterRound(2);

	    }


		private Attack WindStaff() {
	        Attack a = new Attack("Wind Staff");
	        a.setAttackBonus(5)
	         .setMeleereach(5)
	         .setRollToHitInd(1)
	         .getFirstDamage()
	         .nbrDice(1)
	         .dieType(8)
	         .bonus(3)
	         .damageTypeRefId(RefList.damagetypesbludgeoning);
	        a.getDamage().add(new DamageModel(new NbrOfDice(2), new DieType(10), new DamageType(RefList.damagetypeslightning)));
	        return a;
	    }


	    private Attack WindStaffRanged() {
	        Attack a = Converter.deepCopy(WindStaff());
	        a.setAttackRange(120);
	        a.setMeleereach(0);
	        return a;
	    }
	    
	    // only do spells which have combat value
	    private Action LightningBolt() {
	    	Action a = new com.dndcombat.spells.wizard.LightningBolt().createInitialAction();
	    	a.setLimitedUses(1);				// usually a max per day
	    	a.setSpellSlot(new SpellSlot(0));	// monsters have no spell slots
	    	((com.dndcombat.spells.wizard.LightningBolt) a.getSpell()).baseSlot = 0;  // prevent missing spell slots error
	    	return a;
	    }


}
