var SmartShopping;(()=>{"use strict";var e={20:(e,t)=>{Object.defineProperty(t,"__esModule",{value:!0}),t.logger=void 0;const s={print(){},send(){}};t.logger=s,s.print=(e,t)=>console.log(e,t)},242:(e,t,s)=>{s.r(t),s.d(t,{createNanoEvents:()=>o});let o=()=>({events:{},emit(e,...t){(this.events[e]||[]).forEach((e=>e(...t)))},on(e,t){return(this.events[e]=this.events[e]||[]).push(t),()=>this.events[e]=(this.events[e]||[]).filter((e=>e!==t))}})}},t={};function s(o){var r=t[o];if(void 0!==r)return r.exports;var i=t[o]={exports:{}};return e[o](i,i.exports,s),i.exports}s.d=(e,t)=>{for(var o in t)s.o(t,o)&&!s.o(e,o)&&Object.defineProperty(e,o,{enumerable:!0,get:t[o]})},s.o=(e,t)=>Object.prototype.hasOwnProperty.call(e,t),s.r=e=>{"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})};var o={};(()=>{var e,t,r=o;Object.defineProperty(r,"__esModule",{value:!0}),r.Engine=void 0;const i=s(242),c=s(20),n={isAndroid:()=>{var e;return!!(null===(e=window)||void 0===e?void 0:e.SmartshoppingMessageBridge)},isIOS:()=>{var e,t;return!!(null===(t=null===(e=window)||void 0===e?void 0:e.webkit)||void 0===t?void 0:t.messageHandlers)},iOSBridge:null===(t=null===(e=window)||void 0===e?void 0:e.webkit)||void 0===t?void 0:t.messageHandlers.postMessage,androidBridge:e=>{var t;null===(t=window)||void 0===t||t.SmartshoppingMessageBridge.onMessageReceived(e)},isMobile(){return this.isIOS()||this.isAndroid()},onMessage(e){if(this.isMobile){const t=t=>{console.log(t),e(t.detail,null,(e=>this.sendMessage(e)))};window.addEventListener("SmartShoppingWebViewMessage",t,!1)}else chrome.runtime.onMessage.addListener(e)},sendMessage(e,t){this.isMobile?(this.isIOS()&&this.iOSBridge(JSON.stringify(e)),this.isAndroid()&&this.androidBridge(JSON.stringify(e)),t&&t(null)):chrome.runtime.sendMessage(e,t)}},a=e=>{if("string"==typeof e)return document.querySelector(e);let t=null;for(const s of e.shadowRoots){const e=(t||document).querySelector(s);if(!e){t=null;break}t=e.shadowRoot}return t&&t.querySelector(e.target)},d=e=>null!==e&&!!(e.offsetWidth||e.offsetHeight||e.getClientRects().length),l=e=>{let t;switch(e.type){case"element_visible":t=d(a(e.value)),c.logger.print(`element visible: ${t}`,{command:"parse",code:"doesnt matter"});break;case"or":t=e.operands.map((e=>l(e))).includes(!0);break;case"and":t=!e.operands.map((e=>l(e))).includes(!1);break;case"not":t=!l(e.operands[0])}return t};r.Engine=class{constructor(){this.emitter=(0,i.createNanoEvents)(),this._checkoutState={total:null},this._finalCost={},this._progress="IDLE",this.context={code:"",codeIsValid:!1,value:null,return:0,criticalError:!1,anchor:null,anchorCode:null,anchorStage:null},this._config={version:0,taskId:"",shopId:"",shopName:"",shopUrl:"",checkoutUrl:"",selectorsToCheck:[],extendedLogs:!1,extendedReports:!1,inspect:[],detect:[],apply:[],applyBest:[]},this._observers=[],this._promocodes=[],this._currentCode="",this._detectState={userCode:"",isValid:!1},this._bestCode="",this._checkout=!1;n.onMessage(((e,t,s)=>(((e,t,s)=>{if("smartshopping_check"!==e.type)return;if(null===e.defaultSelectors)return;const o=Object.keys(e.defaultSelectors).find((t=>!e.defaultSelectors[t].map((e=>a(e))).includes(null)));o&&c.logger.print(`e-commerce platform - ${o}`,{command:"handleCheck",code:""}),s(o||null)})(e,0,s),!0))),n.onMessage((async e=>{var t;if("smartshopping_init"===e.type&&JSON.parse(e.config)&&(this.config=JSON.parse(e.config),this.promocodes=null!==(t=e.promocodes)&&void 0!==t?t:[],e.persistedState&&(this.context=e.persistedState.context,this.checkoutState=e.persistedState.checkoutState,this.finalCost=e.persistedState.finalCost,this.promocodes=e.persistedState.promocodes),this.checkout=e.checkout,e.checkout&&!e.persistedState&&c.logger.send({type:"checkout",shop:this.config.shopId}),e.persistedState))if("complete"===document.readyState)switch(e.persistedState.context.anchorStage){case"inspect":this.fullCycle();break;case"detect":await this.detect();break;case"apply":await this.apply(),this.applyBest();break;case"applyBest":this.applyBest()}else{let t;switch(e.persistedState.context.anchorStage){case"inspect":t=()=>this.fullCycle();break;case"detect":t=async()=>{await this.detect()};break;case"apply":t=async()=>{await this.apply(),this.applyBest()};break;case"applyBest":t=()=>this.applyBest()}const s=()=>{t(),document.removeEventListener("load",s)};document.addEventListener("load",s)}})),n.onMessage((e=>{"smartshopping_codes"===e.type&&(this.promocodes=e.promocodes)}))}get checkoutState(){return Object.assign({},this._checkoutState)}set checkoutState(e){this._checkoutState=Object.assign({},e),this.emitter.emit("checkoutState",Object.assign({},e),this.state())}get finalCost(){return Object.assign({},this._finalCost)}set finalCost(e){this._finalCost=Object.assign({},e),this.emitter.emit("finalCost",Object.assign({},e),this.state())}get progress(){return this._progress}set progress(e){this._progress=e,this.emitter.emit("progress",e,this.state())}clearObservers(){this._observers.forEach((e=>{e.disconnect()})),setTimeout((()=>{this._observers=[]}),0)}addObserver(e){this._observers.push(e)}get config(){return Object.assign({},this._config)}set config(e){this._config=JSON.parse(JSON.stringify(e)),this.emitter.emit("config",JSON.parse(JSON.stringify(e)),this.state())}get promocodes(){return[...this._promocodes]}set promocodes(e){this._promocodes=[...e],this.emitter.emit("promocodes",[...e],this.state())}get currentCode(){return this._currentCode}set currentCode(e){this._currentCode=e,this.emitter.emit("currentCode",e,this.state())}get detectState(){return Object.assign({},this._detectState)}set detectState(e){this._detectState=e,this.emitter.emit("detectState",e,this.state())}get bestCode(){return this._bestCode}set bestCode(e){this._bestCode=e,this.emitter.emit("bestCode",e,this.state())}get checkout(){return this._checkout}set checkout(e){this._checkout=e,this.emitter.emit("checkout",e,this.state())}state(){return{checkoutState:this.checkoutState,finalCost:this.finalCost,progress:this.progress,config:this.config,promocodes:this.promocodes,detectState:this.detectState,bestCode:this.bestCode,currentCode:this.currentCode,checkout:this.checkout}}subscribe({checkoutState:e=(()=>{}),finalCost:t=(()=>{}),progress:s=(()=>{}),config:o=(()=>{}),promocodes:r=(()=>{}),detectState:i=(()=>{}),bestCode:c=(()=>{}),currentCode:n=(()=>{}),checkout:a=(()=>{})}){return{checkoutState:this.emitter.on("checkoutState",e),finalCost:this.emitter.on("finalCost",t),progress:this.emitter.on("progress",s),config:this.emitter.on("config",o),promocodes:this.emitter.on("promocodes",r),detectState:this.emitter.on("detectState",i),bestCode:this.emitter.on("bestCode",c),currentCode:this.emitter.on("currentCode",n),checkout:this.emitter.on("checkout",a)}}unsubscribe(e){for(const t of Object.keys(e))e[t]()}async commandIf(e,t,s,o){let r=Object.assign({},o);const i={command:"if",code:r.code};try{const o=l(e);c.logger.print(`condition parsed, result: ${o}`,i),r.anchor?(r=await this.execCommands(t,r),r.anchor&&(r=await this.execCommands(s,r))):o?(c.logger.print("executing DO branch",i),r=await this.execCommands(t,r)):(c.logger.print("executing ELSE branch",i),r=await this.execCommands(s,r))}catch(e){r.return++,c.logger.print("CRITICAL: invalid selector",i),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"})}return r}async iterationCodes(e,t){let s=Object.assign({},t);for(const t of this.promocodes)c.logger.print(`executing for code ${t}`,{code:"",command:"iteration_codes"}),this.currentCode=t,s=await this.execCommands(e,Object.assign(Object.assign({},s),{code:t})),s.return=0;return s}async commandWhile(e,t,s){let o=Object.assign({},s);const r={command:"while",code:o.code};for(;;)try{if(o.criticalError||this.context.criticalError)return o.criticalError=!0,o;const s=l(e);if(c.logger.print(`condition parsed, result: ${s}`,r),o.anchor){if(o=await this.execCommands(t,o),o.anchor){c.logger.print("exit while loop",r);break}}else{if(!s){c.logger.print("exit while loop",r);break}c.logger.print("while loop iteration",r),o=await this.execCommands(t,o)}}catch(e){o.return++,c.logger.print("CRITICAL: invalid selector",r),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),c.logger.print("exit while loop",r);break}return o}async Anchor(e,t,s){const o=Object.assign({},s),r={command:"anchor",code:o.code};switch(t){case"before":null===o.anchor&&(await new Promise((t=>{n.sendMessage({type:"smartshopping_persist",persistedState:{context:Object.assign(Object.assign({},o),{anchor:e,anchorCode:o.code,anchorStage:{INSPECT:"inspect",DETECT:"detect",APPLY:"apply","APPLY-BEST":"applyBest"}[this.progress]}),finalCost:this.finalCost,checkoutState:this.checkoutState,promocodes:this.promocodes}},(()=>{t()}))})),c.logger.print(`anchor "${e}"`,r));break;case"after":null===o.anchor&&await new Promise((e=>setTimeout(e,1e6))),o.anchor===e&&o.anchorCode===o.code&&(o.anchor=null,o.anchorCode=null,o.anchorStage=null,c.logger.print(`anchor "${e}" passed for code "${o.code}"`,r))}return o}async commandWait(e,t,s=null){const o=Object.assign({},t),r={command:"wait",code:o.code};if(o.anchor)return o;if(null===s)return c.logger.print(`no response needed, waiting ${e} ms`,r),await new Promise((t=>setTimeout(t,e))),o;c.logger.print("waiting for changes...",r);try{await new Promise(((t,i)=>{let n=!1;s.forEach((s=>{let l,h;switch(s.type){case"added":if(h={childList:!0,subtree:!0},"string"==typeof s.selector)l=new MutationObserver((()=>{const e=a(s.selector);d(e)&&(c.logger.print(`element matching ${s.selector} selector was added`,r),n=!0,l.disconnect(),t(!0))})),l.observe(document,h),this.addObserver(l),setTimeout((()=>{n||(l.disconnect(),i(new Error))}),e);else{const o=[];l=new MutationObserver((()=>{const e=a(s.selector);d(e)&&(c.logger.print(`element matching ${s.selector} selector was added`,r),n=!0,o.forEach((e=>e.disconnect())),t(!0))})),l.observe(document,h),this.addObserver(l),o.push(l);let g=null;const{shadowRoots:p}=s.selector;for(const e of p){const i=(g||document).querySelector(e);if(!i)break;g=i.shadowRoot,l=new MutationObserver((()=>{const e=a(s.selector);d(e)&&(c.logger.print(`element matching ${s.selector} selector was added`,r),n=!0,o.forEach((e=>e.disconnect())),t(!0))})),l.observe(g,h),this.addObserver(l),o.push(l)}setTimeout((()=>{n||(o.forEach((e=>e.disconnect())),i(new Error))}),e)}break;case"removed":if(h={childList:!0,subtree:!0},"string"==typeof s.selector)l=new MutationObserver((()=>{const e=a(s.selector);d(e)||(c.logger.print(`element matching ${s.selector} selector was removed`,r),n=!0,l.disconnect(),t(!0))})),l.observe(document,h),this.addObserver(l),setTimeout((()=>{n||(l.disconnect(),i(new Error))}),e);else{const o=[];l=new MutationObserver((()=>{const e=a(s.selector);d(e)||(c.logger.print(`element matching ${s.selector} selector was removed`,r),n=!0,o.forEach((e=>e.disconnect())),t(!0))})),l.observe(document,h),this.addObserver(l),o.push(l);let g=null;s.selector.shadowRoots.forEach((e=>{g=(g||document).querySelector(e).shadowRoot,l=new MutationObserver((()=>{const e=a(s.selector);d(e)||(c.logger.print(`element matching ${s.selector} selector was removed`,r),n=!0,o.forEach((e=>e.disconnect())),t(!0))})),l.observe(g,h),this.addObserver(l),o.push(l)})),setTimeout((()=>{n||(o.forEach((e=>e.disconnect())),i(new Error))}),e)}break;case"changed":h={characterData:!0,attributes:!0,childList:!0,subtree:!0},l=new MutationObserver((e=>{e.forEach((()=>{c.logger.print(`element matching ${s.selector} selector was changed`,r),n=!0,l.disconnect(),t(!0)}))}));const g=a(s.selector);null===g?(c.logger.print("CRITICAL: invalid selector",r),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),o.criticalError=!0,i(new Error)):(l.observe(g,h),setTimeout((()=>{n||(l.disconnect(),i(new Error))}),e))}}))}))}catch(t){c.logger.print(`${e} ms timeout expired, fail`,r),o.return++}return o}commandInsert(e,t,s){const o=Object.assign({},t),r=void 0===s?o.code:s,i={command:"insert",code:o.code};if(o.anchor)return o;const n=a(e);return n?(n.focus(),n.value=r,n.dispatchEvent(new KeyboardEvent("touchstart",{bubbles:!0})),n.dispatchEvent(new Event("input",{bubbles:!0,cancelable:!0})),n.dispatchEvent(new Event("change",{bubbles:!0,cancelable:!0})),c.logger.print(`code ${r} injected in element matching ${e} selector`,i),o):(c.logger.print("CRITICAL: invalid selector",i),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),o.criticalError=!0,o)}commandApply(e,t){const s=Object.assign({},t),o={command:"apply",code:s.code};if(s.anchor)return s;const r=a(e.selector);return r?("click"===e.type&&["BUTTON","INPUT","A"].includes(r.nodeName)?r.click():r.dispatchEvent(new Event(e.type,{bubbles:!0,cancelable:!1,composed:!0})),c.logger.print(`action "${e.type}" performed on ${e.selector} element`,o),s):(c.logger.print("CRITICAL: invalid selector",o),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),s.criticalError=!0,s)}async commandInteract(e,t,s,o){const r=Object.assign({},o),i={command:"interact",code:r.code};if(r.anchor)return r;const n=a(e.selector);if(c.logger.print(`ACTION TARGET: ${n}`,i),!n)return c.logger.print("CRITICAL: invalid selector",i),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),r.criticalError=!0,r;c.logger.print(`trying to ${e.type} ${e.selector} element and wait...`,i);try{await new Promise(((o,l)=>{let h=!1;t.forEach((e=>{let t,n;switch(e.type){case"added":if(n={childList:!0,subtree:!0},"string"==typeof e.selector)t=new MutationObserver((()=>{const s=a(e.selector);d(s)&&(c.logger.print(`element matching ${e.selector} selector was added`,i),h=!0,t.disconnect(),o(!0))})),t.observe(document,n),this.addObserver(t),setTimeout((()=>{h||(t.disconnect(),l(new Error))}),s);else{const r=[];t=new MutationObserver((()=>{const t=a(e.selector);d(t)&&(c.logger.print(`element matching ${e.selector} selector was added`,i),h=!0,r.forEach((e=>e.disconnect())),o(!0))})),t.observe(document,n),this.addObserver(t),r.push(t);let g=null;const{shadowRoots:p}=e.selector;for(const s of p){const l=(g||document).querySelector(s);if(!l)break;g=l.shadowRoot,t=new MutationObserver((()=>{const t=a(e.selector);d(t)&&(c.logger.print(`element matching ${e.selector} selector was added`,i),h=!0,r.forEach((e=>e.disconnect())),o(!0))})),t.observe(g,n),this.addObserver(t),r.push(t)}setTimeout((()=>{h||(r.forEach((e=>e.disconnect())),l(new Error))}),s)}break;case"removed":if(n={childList:!0,subtree:!0},"string"==typeof e.selector)t=new MutationObserver((()=>{const s=a(e.selector);d(s)||(c.logger.print(`element matching ${e.selector} selector was removed`,i),h=!0,t.disconnect(),o(!0))})),t.observe(document,n),this.addObserver(t),setTimeout((()=>{h||(t.disconnect(),l(new Error))}),s);else{const r=[];t=new MutationObserver((()=>{const t=a(e.selector);d(t)||(c.logger.print(`element matching ${e.selector} selector was removed`,i),h=!0,r.forEach((e=>e.disconnect())),o(!0))})),t.observe(document,n),this.addObserver(t),r.push(t);let g=null;e.selector.shadowRoots.forEach((s=>{g=(g||document).querySelector(s).shadowRoot,t=new MutationObserver((()=>{const t=a(e.selector);d(t)||(c.logger.print(`element matching ${e.selector} selector was removed`,i),h=!0,r.forEach((e=>e.disconnect())),o(!0))})),t.observe(g,n),this.addObserver(t),r.push(t)})),setTimeout((()=>{h||(r.forEach((e=>e.disconnect())),l(new Error))}),s)}break;case"changed":n={characterData:!0,attributes:!0,childList:!0,subtree:!0},t=new MutationObserver((s=>{s.forEach((()=>{c.logger.print(`element matching ${e.selector} selector was changed`,i),h=!0,t.disconnect(),o(!0)}))}));const g=a(e.selector);g?(t.observe(g,n),this.addObserver(t),setTimeout((()=>{h||(t.disconnect(),l(new Error))}),s)):(c.logger.print("CRITICAL: invalid selector",i),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),r.criticalError=!0,l(new Error))}})),"click"===e.type&&["BUTTON","INPUT","A"].includes(n.nodeName)?n.click():n.dispatchEvent(new Event(e.type,{bubbles:!0,cancelable:!1,composed:!0})),c.logger.print(`action "${e.type}" performed on ${e.selector} element`,i)}))}catch(e){c.logger.print(`${s} ms timeout expired, fail`,i),r.return++}return this.clearObservers(),r}commandExtract(e,t,s,o,r,i){const n=Object.assign({},i),d={command:"extract",code:n.code};if(n.anchor)return n;if("undefinedCode"===o)return n.value="UNDEFINED_CODE",n.codeIsValid=null!=r&&r,c.logger.print('value "UNDEFINED_CODE" was extracted',d),n;const l=a(e);if(!l)return c.logger.print("CRITICAL: invalid selector",d),c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),n.criticalError=!0,n;let h;if(h=(null==t?void 0:t.startsWith("data-"))?l.dataset[t.slice(5).split("-").map(((e,t)=>0===t?e:e.charAt(0).toUpperCase()+e.slice(1))).join("")]:t?l[t]:l.textContent,s)switch(s.position){case"before":h=h.slice(0,h.indexOf(s.rightEdge));break;case"after":h=h.slice(h.lastIndexOf(s.leftEdge)+s.leftEdge.length);break;case"between":h=h.slice(h.indexOf(s.leftEdge)+s.leftEdge.length,h.indexOf(s.rightEdge,h.indexOf(s.leftEdge)+s.leftEdge.length))}switch(o){case"number":h=h.replace(/[^\d.,]/gi,""),h=h.replace(/[,]/gi,"."),h=Array.from(h).map(((e,t)=>"."===e&&h.length-t>3?"":e)).join("");break;case"decimalNumberWithoutDot":h=h.replace(/[^\d]/gi,""),h=Array.from(h).map(((e,t)=>h.length-t==2?".".concat(e):e)).join("");break;case"code":h=h.toUpperCase(),h=h.replace(/\s/gi,"")}return n.value=h,"code"===o&&null!==r&&(n.codeIsValid=r),c.logger.print(`value "${n.value}" was extracted from ${e} elements`,d),n}commandStore(e,t){const s=Object.assign({},t),o={command:"store",code:s.code};if(s.anchor)return s;if(!s.value)return c.logger.print("no value to store",o),s.return++,s;switch(e){case"code":s.code=s.value,c.logger.print(`current code is ${s.value}`,o);break;case"total":this.checkoutState=Object.assign(Object.assign({},this.checkoutState),{total:parseFloat(s.value)}),c.logger.print(`stored ${parseFloat(s.value)} in TOTAL`,o);break;case"final":this.finalCost=Object.assign(Object.assign({},this.finalCost),{[s.code]:parseFloat(s.value)}),c.logger.print(`stored ${parseFloat(s.value)} in FINAL for code ${s.code}`,o)}return s}async execCommands(e,t){let s=Object.assign({},t);const o={command:"EXEC",code:""};for(const t of e){if(s.return)return s.return--,c.logger.print("RETURN",o),s;if(s.criticalError||this.context.criticalError)return s.criticalError=!0,s;switch(t.type){case"anchor":s=await this.Anchor(t.anchor,t.place,s);break;case"command_if":s=await this.commandIf(t.condition,t.do,t.else,s);break;case"iteration_codes":s=await this.iterationCodes(t.do,s);break;case"command_while":s=await this.commandWhile(t.condition,t.do,s);break;case"command_wait":s=void 0!==t.response?await this.commandWait(t.timeout,s,t.response):await this.commandWait(t.timeout,s);break;case"command_insert":s=void 0!==t.value?this.commandInsert(t.selector,s,t.value):this.commandInsert(t.selector,s);break;case"command_apply":s=this.commandApply(t.action,s);break;case"command_interact":s=await this.commandInteract(t.action,t.response,t.timeout,s);break;case"command_store":s=this.commandStore(t.target,s);break;case"command_extract":s=this.commandExtract(t.selector,t.target,t.cut,t.format,t.codeIsValid,s);break;case"command_return":s.return+=t.level}}return s}findBestCode(e){const t=Math.min(...Object.values(e));let s="";if(t!==this.checkoutState.total){for(const o of Object.keys(e))e[o]===t&&(s=o);t>this.checkoutState.total&&this.config.extendedReports&&c.logger.send({type:"report",shop:this.config.shopId,url:window.location.href,total:this.checkoutState.total.toFixed(2),discount:(this.checkoutState.total-this.finalCost[s]).toFixed(2),codes:JSON.stringify(e),layoutPage:""}),this.bestCode=s}}async checkSelectors(){const e={command:"SELECTORS_CHECK",code:""};this.config.selectorsToCheck.forEach((t=>{null===a(t)&&(c.logger.print(`invalid selector: "${t}"`,e),this.context.criticalError=!0)}))}async inspect(){let e=Object.assign({},this.context);if(this.progress="INSPECT",c.logger.print("Inspecting...",{command:"STEP_INSPECT",code:""}),e=await this.execCommands(this.config.inspect,e),this.context=Object.assign({},e),await this.checkSelectors(),e=Object.assign({},this.context),e.criticalError)return c.logger.send({type:"error",shop:this.config.shopId,message:"invalid selector"}),void(this.progress="ERROR");e.anchor||c.logger.send({type:"show",shop:this.config.shopId,total:this.checkoutState.total.toFixed(2)}),this.progress="INSPECT_END"}async detect(){let e=Object.assign({},this.context);if(e.criticalError)return this.context=Object.assign({},e),void(this.progress="ERROR");for(this.progress="DETECT",c.logger.print("Detecting...",{command:"STEP_DETECT",code:""});;){if(e=await this.execCommands(this.config.detect,e),e.criticalError||this.context.criticalError){this.context=Object.assign({},e);break}e.anchor||""===e.code||e.code===this.detectState.userCode||(c.logger.send({type:"detect",shop:this.config.shopId,code:e.code,valid:e.codeIsValid}),this.detectState={userCode:e.code,isValid:e.codeIsValid},c.logger.print("Detected user code",{command:"",code:e.code})),this.context=Object.assign({},e)}}async apply(){let e=Object.assign({},this.context);if(e.criticalError)return this.context=Object.assign({},e),void(this.progress="ERROR");e.anchor||c.logger.send({type:"start",shop:this.config.shopId}),this.progress="APPLY",c.logger.print("Applying codes...",{command:"STEP_APPLY",code:""}),e=await this.execCommands(this.config.apply,e),this.context=Object.assign({},e),this.progress="APPLY_END"}async applyBest(){const e=Object.assign({},this.context),t={command:"STEP_APPLY_BEST",code:""};e.criticalError?this.progress="ERROR":(this.progress="APPLY-BEST",this.findBestCode(this.finalCost),""!==this.bestCode?(c.logger.print(`Applying best code... [${this.bestCode}]`,t),await this.execCommands(this.config.applyBest,Object.assign(Object.assign({},e),{code:this.bestCode,value:null,return:0,criticalError:!1})),c.logger.send({type:"success",shop:this.config.shopId,code:this.config.extendedLogs?this.bestCode:"",total:this.checkoutState.total.toFixed(2),discount:(this.checkoutState.total-this.finalCost[this.bestCode]).toFixed(2)})):c.logger.print("All the coupons failed :[",t),n.sendMessage({type:"smartshopping_clear"}),this.progress="APPLY-BEST_END")}notifyAboutShowModal(){c.logger.send({type:"show_slider",shop:this.config.shopId})}notifyAboutCloseModal(){c.logger.send({type:"close_slider",shop:this.config.shopId})}abort(){this.context.criticalError=!0,this.progress="CANCEL",c.logger.send({type:"abort",shop:this.config.shopId})}async fullCycle(){await this.inspect(),await this.apply(),await this.applyBest()}}})(),SmartShopping=o})();