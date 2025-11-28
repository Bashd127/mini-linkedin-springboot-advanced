const API='/api';
let token=null;
async function api(path, method='GET', body=null){
  const headers = {};
  if(token) headers['Authorization']='Bearer '+token;
  if(body) headers['Content-Type']='application/json';
  const res = await fetch(API+path, {method, headers, body: body?JSON.stringify(body):null});
  return res.ok?res.json():Promise.reject(await res.text());
}
document.getElementById('loginBtn').onclick = async ()=>{
  const email=document.getElementById('email').value, password=document.getElementById('password').value;
  try{ const r = await api('/auth/login','POST',{email,password}); token=r.token; document.getElementById('auth').classList.add('hidden'); document.getElementById('feed').classList.remove('hidden'); loadFeed(); }catch(e){ alert('Login failed: '+e) }
};
document.getElementById('registerBtn').onclick = async ()=>{
  const email=document.getElementById('email').value, password=document.getElementById('password').value;
  try{ const r = await api('/auth/register','POST',{email,password}); token=r.token; document.getElementById('auth').classList.add('hidden'); document.getElementById('feed').classList.remove('hidden'); loadFeed(); }catch(e){ alert('Register failed: '+e) }
};
document.getElementById('postBtn').onclick = async ()=>{
  const content=document.getElementById('postContent').value;
  try{ await api('/posts','POST',{authorId:1, content}); document.getElementById('postContent').value=''; loadFeed(); }catch(e){ alert('Post failed: '+e) }
};
async function loadFeed(){
  try{
    const posts = await api('/posts');
    const container=document.getElementById('posts'); container.innerHTML='';
    posts.forEach(p=>{
      const div=document.createElement('div'); div.className='card';
      div.innerHTML=`<b>${p.author? p.author.fullName : 'Unknown'}</b><p>${p.content}</p><small>Likes: ${p.likeCount}</small>`;
      container.appendChild(div);
    });
  }catch(e){ console.error(e) }
}