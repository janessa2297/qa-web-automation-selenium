(function () {
  function enhance() {
    const main = document.querySelector('.main-content');
    if (!main || document.querySelector('.qa-overview')) return;
    const tests = Array.from(document.querySelectorAll('.test-view .test-list .test-list-item > .test-item'));
    const counts = {pass: 0, fail: 0, skip: 0};
    tests.forEach(test => { const status = test.getAttribute('status'); if (status in counts) counts[status]++; });
    const total = tests.length;
    const other = total - counts.pass - counts.fail - counts.skip;
    const rate = total ? Math.round(counts.pass / total * 100) : 0;
    const select = (selector, root = document) => root.querySelector(selector);
    const element = (tag, className, text) => {
      const node = document.createElement(tag); node.className = className || '';
      if (text !== undefined) node.textContent = text;
      return node;
    };
    const showScenarios = (status = 'clear', selected = null) => {
      select('#nav-test')?.click();
      select('#status-toggle a[status="clear"]')?.click();
      tests.forEach(test => { test.style.display = status === 'clear' || test.getAttribute('status') === status ? 'block' : 'none'; });
      const first = selected || tests.find(test => test.style.display !== 'none');
      const content = select('.test-view .test-content');
      let empty = select('.qa-filter-empty', content);
      if (!empty) { empty = element('div', 'qa-filter-empty', 'No scenarios match this status. Choose another status or view all scenarios.'); content?.appendChild(empty); }
      empty.hidden = Boolean(first);
      const detail = select('.test-content-detail', content);
      if (detail) detail.hidden = !first;
      if (first) { select('.test-detail', first)?.click(); first.scrollIntoView({block:'nearest'}); }
    };
    const environment = {};
    document.querySelectorAll('.sysenv-container tbody tr').forEach(row => {
      const cells = row.querySelectorAll('td');
      if (cells.length === 2) environment[cells[0].textContent.trim()] = cells[1].textContent.trim();
    });
    const overview = document.createElement('section');
    overview.className = 'qa-overview';
    overview.setAttribute('aria-label', 'Execution overview');
    overview.innerHTML = '<div class="qa-heading"><div><div class="qa-eyebrow">Automation / Execution report</div><h1>Quality at a glance.</h1><p class="qa-subtitle"></p></div><div class="qa-outcome"></div></div><div class="qa-metrics"></div>';
    const metadata = [environment.Browser, environment.Headless === 'true' ? 'Headless browser' : (environment.Headless === 'false' ? 'Visible browser' : ''), environment.Environment].filter(Boolean);
    overview.querySelector('.qa-subtitle').textContent = metadata.join('  /  ') || 'Automated test execution';
    const outcome = overview.querySelector('.qa-outcome');
    outcome.dataset.state = !total ? 'empty' : (counts.fail || counts.skip || other ? 'review' : 'pass');
    outcome.textContent = !total ? 'No tests recorded' : (counts.fail ? 'Failures detected' : (counts.skip || other ? 'Review required' : 'All checks passed'));
    const metrics = [
      ['clear', 'Total scenarios', total, 'View all test cases'],
      ['pass', 'Passed', counts.pass, total ? rate + '% of all scenarios' : 'No scenarios recorded'],
      ['fail', 'Failed', counts.fail, 'Inspect failures and evidence'],
      ['skip', 'Skipped', counts.skip, other ? other + ' additional status(es)' : 'Tests that did not complete']
    ];
    metrics.forEach(([status, label, value, note]) => {
      const button = document.createElement('button');
      button.type = 'button'; button.className = 'qa-metric'; button.dataset.status = status;
      button.innerHTML = '<span class="qa-metric-label"></span><strong class="qa-metric-value"></strong><span class="qa-metric-note"></span>';
      button.querySelector('.qa-metric-label').textContent = label;
      button.querySelector('.qa-metric-value').textContent = value;
      button.querySelector('.qa-metric-note').textContent = note;
      button.setAttribute('aria-label', label + ': ' + value + '. Filter scenarios.');
      button.addEventListener('click', () => showScenarios(status));
      overview.querySelector('.qa-metrics').appendChild(button);
    });
    main.prepend(overview);
    // This overview is derived from the recorded scenarios, never from sample data.
    const dashboard = element('section', 'view qa-dashboard');
    dashboard.setAttribute('aria-label', 'Run dashboard');
    const top = element('div', 'qa-dashboard-grid');
    const health = element('article', 'qa-panel qa-health');
    health.append(element('div', 'qa-panel-kicker', 'Execution health'), element('h2', '', 'Scenario outcomes'));
    const distribution = element('div', 'qa-distribution');
    const ring = element('div', 'qa-ring');
    const passAngle = total ? counts.pass / total * 360 : 0;
    const failAngle = total ? (counts.pass + counts.fail) / total * 360 : 0;
    ring.style.background = total ? `conic-gradient(#18836b 0deg ${passAngle}deg,#c74556 ${passAngle}deg ${failAngle}deg,#d5a347 ${failAngle}deg 360deg)` : '#e7edf5';
    ring.setAttribute('role','img'); ring.setAttribute('aria-label', `${rate}% passed; ${counts.fail} failed; ${counts.skip} skipped; ${other} other`);
    const center = element('div', 'qa-ring-center');
    center.append(element('strong','',rate + '%'), element('span','','PASS RATE')); ring.appendChild(center);
    distribution.appendChild(ring);
    const legend = element('div','qa-legend');
    [['pass','Passed',counts.pass],['fail','Failed',counts.fail],['skip','Skipped',counts.skip]].forEach(([status,label,value]) => {
      const line = element('div','qa-legend-row'); line.dataset.status = status;
      line.append(element('span','qa-dot'),element('span','',label),element('strong','',String(value))); legend.appendChild(line);
    });
    if (other) legend.append(element('div','qa-legend-row',other + ' other outcomes'));
    distribution.appendChild(legend); health.appendChild(distribution);
    const dates = Array.from(document.querySelectorAll('.dashboard-view > .row:first-child .card h3')).slice(0,2).map(node=>node.textContent.trim());
    const parseDate = value => Date.parse(value.replace(' ', 'T'));
    const seconds = dates.length === 2 ? Math.max(0, (parseDate(dates[1])-parseDate(dates[0]))/1000) : NaN;
    const duration = Number.isFinite(seconds) ? (seconds < 1 ? '<1s' : seconds < 60 ? seconds + 's' : Math.floor(seconds/60) + 'm ' + Math.round(seconds%60) + 's') : 'Not recorded';
    const timing = element('div','qa-timing');
    timing.append(element('span','','Wall-clock duration'),element('strong','',duration)); health.appendChild(timing);
    const coverage = element('article','qa-panel qa-coverage');
    coverage.append(element('div','qa-panel-kicker','Coverage by feature'),element('h2','','Every feature, accounted for.'));
    const features = new Map();
    tests.forEach(test => {
      const name = select('.test-detail .name',test)?.textContent.trim() || 'Unnamed scenario';
      const feature = name.includes(' :: ') ? name.split(' :: ')[0].replace(/^\d+\s*\|\s*/, '').replace(/Tests$/, '') : 'Other';
      if (!features.has(feature)) features.set(feature,{total:0,pass:0,fail:0,skip:0});
      const group = features.get(feature); group.total++; const status = test.getAttribute('status'); if(status in group) group[status]++;
    });
    const featureTable = element('table','qa-feature-table');
    featureTable.innerHTML = '<thead><tr><th>Feature</th><th>Distribution</th><th>Passed</th><th>Failed</th><th>Total</th></tr></thead><tbody></tbody>';
    features.forEach((group,name) => {
      const row = element('tr',''); row.appendChild(element('td','qa-feature-name',name));
      const chartCell = element('td',''); const bar = element('div','qa-feature-bar');
      [['pass',group.pass],['fail',group.fail],['skip',group.total-group.pass-group.fail]].forEach(([status,value])=>{const segment=element('span',status);segment.style.width=(value/group.total*100)+'%';bar.appendChild(segment);});
      bar.setAttribute('aria-label',`${group.pass} passed, ${group.fail} failed, ${group.total} total`); chartCell.appendChild(bar); row.appendChild(chartCell);
      row.append(element('td','',String(group.pass)),element('td',group.fail?'qa-failure-number':'',String(group.fail)),element('td','',String(group.total))); select('tbody',featureTable).appendChild(row);
    });
    coverage.appendChild(featureTable); top.append(health,coverage); dashboard.appendChild(top);
    const lower = element('div','qa-dashboard-grid qa-dashboard-lower');
    const attention = element('article','qa-panel qa-attention');
    attention.append(element('div','qa-panel-kicker','Review queue'),element('h2','',counts.fail ? 'Failures to investigate' : (counts.skip || other ? 'Scenarios needing review' : 'No failures to investigate.')));
    const reviewTests = tests.filter(test=>test.getAttribute('status')!=='pass');
    if (!reviewTests.length) attention.append(element('p','qa-panel-copy','Every recorded scenario passed. Explore the scenario list for the steps and assertions behind each result.'));
    reviewTests.slice(0,5).forEach(test=>{
      const button = element('button','qa-review-item',select('.test-detail .name',test)?.textContent.trim()); button.type='button';
      button.appendChild(element('span','',test.getAttribute('status').toUpperCase()));
      button.addEventListener('click',()=>showScenarios(test.getAttribute('status'),test)); attention.appendChild(button);
    });
    const browse = element('button','qa-action',reviewTests.length ? 'Review scenarios →' : 'Explore all scenarios →'); browse.type='button'; browse.addEventListener('click',()=>showScenarios()); attention.appendChild(browse);
    const context = element('article','qa-panel qa-context');
    context.append(element('div','qa-panel-kicker','Run context'),element('h2','','Execution details'));
    const details = element('dl','qa-context-list');
    [['Started',dates[0]||'Not recorded'],['Finished',dates[1]||'Not recorded'],['Browser',environment.Browser||'Not recorded'],['Mode',environment.Headless==='true'?'Headless':environment.Headless==='false'?'Visible browser':'Not recorded'],['Environment',environment.Environment||'Not recorded']].forEach(([label,value])=>{details.append(element('dt','',label),element('dd','',value));});
    context.appendChild(details); lower.append(attention,context); dashboard.appendChild(lower);
    dashboard.appendChild(element('p','qa-report-note','Results reflect this execution only. Skipped and failed scenarios remain visible.'));
    main.appendChild(dashboard);
    const nav = select('.side-nav-menu');
    const overviewLink = select('#nav-dashboard');
    if (nav && overviewLink) {
      const analytics = overviewLink.closest('li').cloneNode(true);
      select('a',analytics).id='nav-analytics'; analytics.setAttribute('onclick',"toggleView('dashboard-view')"); nav.appendChild(analytics);
      overviewLink.closest('li').setAttribute('onclick',"toggleView('qa-dashboard')"); nav.prepend(overviewLink.closest('li'));
    }
    const labels = {'nav-dashboard':'Overview','nav-test':'Scenarios','nav-category':'Features','nav-exception':'Exceptions','nav-analytics':'Analytics'};
    Object.entries(labels).forEach(([id,label])=>{const link=document.getElementById(id);if(link){link.appendChild(element('span','qa-nav-label',label));link.title=label;link.setAttribute('aria-label',label);link.addEventListener('click',()=>{document.querySelectorAll('.qa-current').forEach(node=>node.classList.remove('qa-current'));link.classList.add('qa-current');});}});
    if (overviewLink) overviewLink.classList.add('qa-current');
    const brand = element('span','qa-brand','OrangeHRM'); select('.nav-logo')?.appendChild(brand);
    // Distinguish time metadata from failure indicators in both source and cloned details.
    document.querySelectorAll('.detail-head .info > .badge-success,.detail-head .info > .badge-danger').forEach(badge=>{badge.classList.remove('badge-success','badge-danger');badge.classList.add('qa-time-badge');});
    const enhanceDetails = () => {
      document.querySelectorAll('.test-content .detail-head .info > .badge-success,.test-content .detail-head .info > .badge-danger').forEach(badge=>{badge.classList.remove('badge-success','badge-danger');badge.classList.add('qa-time-badge');});
      document.querySelectorAll('.test-content .event-row').forEach(row=>{if(select('.fail-bg',row))row.classList.add('qa-failed-step');});
      document.querySelectorAll('.test-content img').forEach(img=>{if(!img.alt)img.alt='Screenshot captured at scenario completion';});
      document.querySelectorAll('.test-content a[data-featherlight="image"]').forEach(link=>{
        if (link.getAttribute('href')?.startsWith('data:image/') && !select('img',link)) {
          link.classList.add('qa-evidence');
          const image = document.createElement('img'); image.src=link.getAttribute('href'); image.alt='Scenario screenshot. Select to open full-size evidence.'; image.loading='lazy';
          link.appendChild(image);
          const badge = select('.badge',link); if(badge) badge.textContent='Open full-size screenshot';
        }
      });
      const pane = select('.test-view .test-content-detail');
      if (pane && select('.text-fail',pane) && !select('.qa-failure-brief',pane)) {
        const failures = Array.from(pane.querySelectorAll('.qa-failed-step td:last-child')).map(node=>node.textContent.trim()).filter(Boolean);
        if (failures.length) {
          const brief = element('aside','qa-failure-brief');
          const summary = failures.join('\n').split(/\nBuild info:/)[0].slice(0,650);
          brief.append(element('div','qa-panel-kicker','Failure context'),element('h3','','Recorded failure'),element('p','',summary));
          const logBody = Array.from(pane.querySelectorAll('.detail-body')).find(node=>select('table',node));
          (logBody || select('.detail-body',pane))?.prepend(brief);
        }
      }
    };
    const detailPane = select('.test-view .test-content-detail');
    if (detailPane) new MutationObserver(enhanceDetails).observe(detailPane,{childList:true,subtree:true});
    enhanceDetails();
    const openOverview = () => window.setTimeout(() => {
      if (typeof window.toggleView === 'function') window.toggleView('qa-dashboard');
    }, 0);
    if (document.readyState === 'complete') openOverview();
    else window.addEventListener('load', openOverview, {once:true});
    const logo = document.querySelector('.header .logo');
    if (logo) logo.textContent = 'OH';
    const search = document.getElementById('search-tests');
    if (search) { search.placeholder = 'Search test scenarios…'; search.setAttribute('aria-label','Search test scenarios'); }
  }
  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', enhance);
  else enhance();
})();
