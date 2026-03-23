const API = '';
let token = localStorage.getItem('token');
let currentCategory = null;
let currentQuestion = null;
let questionCount = 0;

window.onload = async () => {
  checkToken();
  renderHeader();
  await loadCategories();
  connectWebSocket();
};

function checkToken() {
  const params = new URLSearchParams(window.location.search);
  const t = params.get('token');
  if (t) {
    token = t;
    localStorage.setItem('token', t);
    window.history.replaceState({}, '', '/');
    showToast('✅ 로그인 성공!');
  }
}

function renderHeader() {
  const nav = document.getElementById('headerNav');
  if (token) {
    nav.innerHTML = `
      <button class="nav-btn" onclick="showPage('ranking');loadRanking()">[ RANKING ]</button>
      <button class="nav-btn" onclick="showPage('wrongnote');loadWrongNotes()">[ 오답노트 ]</button>
      <span class="user-badge">// LOGGED IN</span>
      <button class="nav-btn red" onclick="logout()">[ LOGOUT ]</button>
    `;
  } else {
    nav.innerHTML = `
      <button class="nav-btn" onclick="showPage('ranking');loadRanking()">[ RANKING ]</button>
      <button class="nav-btn green" onclick="login()">[ LOGIN ]</button>
    `;
  }
}

function login() { window.location.href = `${API}/oauth2/authorization/google`; }
function logout() { localStorage.removeItem('token'); token = null; window.location.reload(); }
function goHome() { currentCategory=null; currentQuestion=null; questionCount=0; showPage('main'); }

function showPage(name) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById(`page-${name}`).classList.add('active');
}

async function loadCategories() {
  try {
    const res = await fetch(`${API}/api/quiz/categories`);
    const cats = await res.json();
    const icons = {SPRING:'🌱',JAVA:'☕',DATABASE:'🗄️',ALGORITHM:'⚡'};
    const descs = {SPRING:'Spring Framework',JAVA:'Java Language',DATABASE:'DB & SQL',ALGORITHM:'Algo & DS'};
    document.getElementById('categoryGrid').innerHTML = cats.map(c => `
      <div class="cat-card" onclick="startQuiz(${c.id},'${c.name}')">
        <div class="cat-icon">${icons[c.name]||'📚'}</div>
        <div class="cat-name">${c.name}</div>
        <div class="cat-desc">${descs[c.name]||c.description||''}</div>
      </div>
    `).join('');
  } catch(e) { showToast('❌ 카테고리 로드 실패'); }
}

async function startQuiz(categoryId, categoryName) {
  currentCategory = {id:categoryId, name:categoryName};
  questionCount = 0;
  showPage('quiz');
  window.scrollTo({top:0, behavior:'smooth'});
  await loadQuestion();
}

async function loadQuestion() {
  document.getElementById('qText').innerHTML = '<span class="loader"></span> 로딩중...';
  document.getElementById('optionsWrap').innerHTML = '';
  document.getElementById('resultBox').className = 'result-box';
  document.getElementById('btnNext').classList.add('hidden');

  try {
    const headers = token ? {'Authorization':`Bearer ${token}`} : {};
    const res = await fetch(`${API}/api/quiz/${currentCategory.id}`, {headers});
    currentQuestion = await res.json();
    questionCount++;

    document.getElementById('quizTag').textContent = currentCategory.name;
    document.getElementById('quizCount').textContent = `// Q.${questionCount}`;
    document.getElementById('qText').textContent = currentQuestion.question;

    const opts = [currentQuestion.option1, currentQuestion.option2, currentQuestion.option3, currentQuestion.option4];
    document.getElementById('optionsWrap').innerHTML = opts.map((o,i) => `
      <button class="opt-btn" onclick="submitAnswer(${i+1})">
        <span class="opt-num">${i+1}</span><span>${o}</span>
      </button>
    `).join('');
  } catch(e) { showToast('❌ 문제 로드 실패'); }
}

async function submitAnswer(selected) {
  document.querySelectorAll('.opt-btn').forEach(b => b.disabled = true);
  try {
    const headers = {'Content-Type':'application/json', ...(token ? {'Authorization':`Bearer ${token}`} : {})};
    const res = await fetch(`${API}/api/quiz/submit`, {
      method:'POST', headers,
      body: JSON.stringify({
        questionId: currentQuestion.questionId,
        answer: selected,
        currentCategoryId: currentCategory.id
      })
    });
    const result = await res.json();
    showResult(result, selected);
   
  } catch(e) { 
    showToast('❌ 제출 실패'); 
  }
  finally {
     window.scrollTo({top: document.body.scrollHeight, behavior:'smooth'});
  }
}

function showResult(result, selected) {
  const btns = document.querySelectorAll('.opt-btn');
  btns[result.answer-1].classList.add('correct');
  if (!result.correct) btns[selected-1].classList.add('wrong');

  const box = document.getElementById('resultBox');
  box.className = `result-box show ${result.correct?'correct':'wrong'}`;
  const status = document.getElementById('resultStatus');
  status.className = `result-status ${result.correct?'correct':'wrong'}`;
  status.textContent = result.correct ? '✓ CORRECT' : '✗ WRONG';
  document.getElementById('resultAnswer').textContent = `// 정답: ${result.answer}번`;
  document.getElementById('resultExp').textContent = result.explanation;
  document.getElementById('btnNext').classList.remove('hidden');
}

function nextQuestion() { loadQuestion(); window.scrollTo({top:0, behavior:'smooth'}); }

async function loadWrongNotes() {
  if (!token) {
    document.getElementById('wnList').innerHTML = `
      <div class="login-required">
        <div class="empty-icon">🔒</div>
        <h3>로그인이 필요합니다</h3>
        <p>오답노트는 로그인 유저만 이용할 수 있어요</p>
        <button class="btn green" onclick="login()">[ LOGIN ]</button>
      </div>`;
    return;
  }
  try {
    const res = await fetch(`${API}/api/wrong-notes`, {headers:{'Authorization':`Bearer ${token}`}});
    const notes = await res.json();
    if (!notes.length) {
      document.getElementById('wnList').innerHTML = `<div class="empty-state"><div class="empty-icon">🎉</div><div>오답노트가 비어있어요!</div></div>`;
      return;
    }
    const opts = (n, a) => [n.option1,n.option2,n.option3,n.option4][a-1]||'';
    document.getElementById('wnList').innerHTML = notes.map(n => `
      <div class="wn-card" id="wn-${n.id}">
        <div class="wn-top">
          <div class="wn-question">${n.question}</div>
          <div class="wn-meta">
            <span class="wn-count">✗ ${n.wrongCount}회</span>
            <button class="wn-del" onclick="deleteWN(${n.id})">[ 삭제 ]</button>
          </div>
        </div>
        <div class="wn-answer">// 정답: ${n.answer}번. ${opts(n,n.answer)}</div>
        <div class="wn-exp">${n.explanation}</div>
      </div>
    `).join('');
  } catch(e) { showToast('❌ 오답노트 로드 실패'); }
}

async function deleteWN(id) {
  try {
    await fetch(`${API}/api/wrong-notes/${id}`, {method:'DELETE', headers:{'Authorization':`Bearer ${token}`}});
    document.getElementById(`wn-${id}`)?.remove();
    showToast('🗑️ 삭제됐어요!');
  } catch(e) { showToast('❌ 삭제 실패'); }
}

async function loadRanking() {
  try {
    const res = await fetch(`${API}/api/ranking`);
    const list = await res.json();
    const medals = ['🥇','🥈','🥉'];
    const cls = ['r1','r2','r3'];
    document.getElementById('rankingList').innerHTML = list.length
      ? list.map((r,i) => `
          <div class="rank-card ${cls[i]||''}">
            <div class="rank-num">${medals[i]||r.rank}</div>
            <div class="rank-info">
              <div class="rank-name">${r.nickname}</div>
              <div class="rank-stats">${r.correctCount}정답 / ${r.totalCount}문제</div>
            </div>
            <div class="rank-rate">${r.correctRate}%</div>
          </div>`).join('')
      : `<div class="empty-state"><div class="empty-icon">🏆</div><div>아직 랭킹 데이터가 없어요!</div></div>`;
  } catch(e) { showToast('❌ 랭킹 로드 실패'); }
}

function connectWebSocket() {
  try {
    const sock = new SockJS(`${API}/ws`);
    const client = Stomp.over(sock);
    client.debug = null;
    client.connect({}, () => {
      client.subscribe('/topic/activity', msg => addFeed(JSON.parse(msg.body)));
    });
  } catch(e) {}
}

function addFeed(data) {
  const body = document.getElementById('feedBody');
  const conn = body.querySelector('[style*="text3"]');
  if (conn) conn.remove();
  const el = document.createElement('div');
  el.className = `feed-item ${data.correct?'correct':'wrong'}`;
  const now = new Date();
  const t = `${String(now.getHours()).padStart(2,'0')}:${String(now.getMinutes()).padStart(2,'0')}:${String(now.getSeconds()).padStart(2,'0')}`;
  el.innerHTML = `${data.message}<div class="feed-time">${t}</div>`;
  body.insertBefore(el, body.firstChild);
  while(body.children.length > 50) body.removeChild(body.lastChild);
}

function showToast(msg) {
  const el = document.getElementById('toast');
  el.textContent = msg;
  el.classList.add('show');
  setTimeout(() => el.classList.remove('show'), 3000);
}