package com.dndcombat.monsters.v;

import com.dndcombat.creatures.model.DamageModel;
import com.dndcombat.fight.model.AttackResult;
import com.dndcombat.fight.model.GameState;
import com.dndcombat.fight.model.ModifierType;
import com.dndcombat.fight.model.Player;
import com.dndcombat.fight.modifiertypes.DefaultModifierType;
import com.dndcombat.model.Action;
import com.dndcombat.model.Attack;
import com.dndcombat.model.Creature;
import com.dndcombat.model.Damage;
import com.dndcombat.model.RefList;
import com.dndcombat.model.SaveDC;
import com.dndcombat.monster.actions.WithinXFeetEffect;
import java.util.Arrays;
import java.util.List;

public class VampireSpawn extends Player {
    
    private static final long serialVersionUID = 1L;

    public VampireSpawn() {
        super(new Creature());
        create();
    }

    public void create() {
        getCreature().setCreatureName("Vampire Spawn");
        setPlayersSide(false);
        getCreature().setImageUrl("/assets/monsters/MM/Vampire Spawn.webp");

        // Ability Scores
        str(16).dex(16).con(16).inte(11).wis(10).cha(12);

        // Saving Throws
        strSave(+3).dexSave(+6).conSave(+3).intSave(+0).wisSave(+3).chaSave(+1);
    
        // Combat Stats
        ac(16).init(+3).hp(90).speed(30).cr(5);

        // Size and Type
        size(RefList.creaturesizesmedium).type(RefList.creaturetypesundead);

        // Damage Resistances
        damageResist(RefList.damagetypesnecrotic);

        
        // Actions
        addAction(new Action("Multiattack", RefList.actiontypesmainaction)
                .addAttack(Claw())
                .addAttack(Claw())
                .addAttack(Bite()));

        addAction(new Action("Claw", RefList.actiontypesmainaction)
                .addAttack(Claw()));
        
        addAction(new Action("Bite", RefList.actiontypesmainaction)
                .addAttack(Bite()));
        
        getCreature().addModifierType(drain());
      	}
          
          private ModifierType drain() {

      	  DefaultModifierType mt = new DefaultModifierType("Bite") {
              	 
      			private static final long serialVersionUID = 1L;
      			@Override
      			public boolean applyDamageOnAttackHit(Player source, Action action, Attack attack, Player target, List<Damage> list, AttackResult attackResult, GameState state, List<Damage> damageSoFar) {
           		   
              		if (source.isGrappling(target)) {
                  		int total = 0;
                  		for (Damage d : damageSoFar) {
                  			if (d.damageTypeRefId == RefList.damagetypesnecrotic) {
                  				total += d.dmg;
                  			}
                  		}
                  		if (total > 0) {
                  		    state.addActionLog(source.getName() + " drains " + total);
                  		    source.heal(Arrays.asList(new Damage(total, RefList.damagetypesheal)), state);
                  		    target.reduceMaxHp(total, state);
                     		state.addActionLog(target.getName() + " max hp reduced to " + target.getStatus().getMaxHitPoints());
                 		  
                  		}
             		} else {
             			state.addActionLog(source.getName() + " unable to bite " + target.getName());;
             		}
              		return false;
              	}
              };
              return mt;
          }


    private Attack Claw() {
        Attack a = new Attack("Claw");
        a.setAttackBonus(6)
         .setMeleereach(5)
         .setRollToHitInd(1)
         .setGrappleOnHit(true)
         .setGrappleDc(13)
         .setGrappleMediumOrSmaller(true)
         .getFirstDamage()
         .nbrDice(2)
         .dieType(4)
         .bonus(3)
         .damageTypeRefId(RefList.damagetypesslashing);
        return a;
    }

    private Attack Bite() {
        Attack a = new Attack("Bite");
        a.setMustBeGrappledByMe(true);
        SaveDC save = new SaveDC().dc(14).saveVsRefId(RefList.savingthrowvsconstitution);
        WithinXFeetEffect w = new WithinXFeetEffect(5, a.getAttackName(), Arrays.asList(
            new DamageModel().nbrDice(1).dieType(4).bonus(3).damageTypeRefId(RefList.damagetypespiercing),
            new DamageModel().nbrDice(3).dieType(6).damageTypeRefId(RefList.damagetypesnecrotic)
        ), save, 0);
        w.maxNbrTargets = 1;
        a.setSpell(w);
        return a;
    }
}
