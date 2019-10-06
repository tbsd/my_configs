filetype off                  " required
" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()
" alternatively, pass a path where Vundle should install plugins
"call vundle#begin('~/some/path/here')
" let Vundle manage Vundle, required
Plugin 'VundleVim/Vundle.vim'
Plugin 'ervandew/supertab'
Plugin 'dhruvasagar/vim-table-mode'
Plugin 'tpope/vim-repeat'
Plugin 'scrooloose/nerdcommenter'
Plugin 'flazz/vim-colorschemes'
Plugin 'jacquesbh/vim-showmarks'
Plugin 'jiangmiao/auto-pairs'
Plugin 'Yggdroot/indentLine'
Plugin 'Lokaltog/vim-powerline'
Plugin 'scrooloose/nerdtree'
Plugin 'xolox/vim-misc'
Plugin 'xolox/vim-session'
Plugin 'vim-scripts/matchit.zip'
Plugin 'heavenshell/vim-pydocstring'
Plugin 'vim-syntastic/syntastic'
" All of your Plugins must be added before the following line
call vundle#end()            " required
"filetype plugin indent on    " required
" To ignore plugin indent changes, instead use:
filetype plugin on

" CUSTOM SETTINGS
" split settings
set splitbelow
" set splitright
" bash-like completion
set wildmode=longest,list:longest
" set wildmenu
" color scheme and other color stuff
colorscheme antares
"Проблема красного на красном при spellchecking-е решается такой строкой в .vimrc
highlight SpellBad ctermfg=Black ctermbg=Red
"" Подсвечивать табы и пробелы в конце строки
set list " включить подсветку
set listchars=tab:>-,trail:- " установить символы, которыми будет осуществляться подсветка
" hilight line with the coursor
set cursorline
" cursor color hack
au ColorScheme * hi Search ctermfg=Yellow ctermbg=Black
" hilight area behind 80 column
let &colorcolumn=join(range(81,999),",")
syntax on " включить подсветку синтаксиса
" shift
set shiftwidth=2 " размер отступов (нажатие на << или >>)
set softtabstop=2
set tabstop=2 " ширина табуляции
set autoindent " ai - включить автоотступы (копируется отступ предыдущей строки)
"set expandtab " преобразовать табуляцию в пробелы
set smartindent " Умные отступы (например, автоотступ после {)
" формат файла по умолчанию (влияет на окончания строк) - будет перебираться в указанном порядке
set fileformat=unix
" search
set ignorecase " ics - поиск без учёта регистра символов
set smartcase " - если искомое выражения содержит символы в верхнем регистре - ищет с учётом регистра, иначе - без учёта
set nohlsearch " (не)подсветка результатов поиска (после того, как поиск закончен и закрыт)
set incsearch " поиск фрагмента по мере его набора
" status line
set laststatus=2 " всегда показывать строку состояния
" different stuff
filetype on
filetype plugin on
filetype indent on
set scrolloff=15 " сколько строк внизу и вверху экрана показывать при скроллинге
set hidden " не выгружать буфер когда переключаешься на другой
set mousehide " скрывать мышь в режиме ввода текста
set showcmd " показывать незавершенные команды в статусбаре (автодополнение ввода)
set mps+=<:> " показывать совпадающие скобки для HTML-тегов
set showmatch " показывать первую парную скобку после ввода второй
set autoread " перечитывать изменённые файлы автоматически
" set t_Co=256 " использовать больше цветов в терминале
" set confirm " использовать диалоги вместо сообщений об ошибках
au BufWinLeave *.* silent mkview " при закрытии файла сохранить 'вид'
au BufWinEnter *.* silent loadview " при открытии - восстановить сохранённый
set noswapfile " не использовать своп-файл (в него скидываются открытые буферы)
" set browsedir=current
set backup " включить сохранение резервных копий
set title " показывать имя буфера в заголовке терминала
" подсвечивает все слова, которые совпадают со словом под курсором.
autocmd CursorMoved * silent! exe printf("match Search /\\<%s\\>/", expand('<cword>'))
"Save marks through restarts
set viminfo='1000,f1
" turn hybrid line numbers on
au VimEnter * set number
au VimEnter * set relativenumber
" Sytax hilighting
let g:ycm_confirm_extra_conf=0

" Hotkeys
" close buffer
nnoremap <F4> :bd<cr>
" split navigation
nnoremap <C-J> <C-W><C-J>
nnoremap <C-K> <C-W><C-K>
nnoremap <C-L> <C-W><C-L>
nnoremap <C-H> <C-W><C-H>
" предыдущий буфер
map <F1> :bp<cr>
vmap <F1> <esc>:bp<cr>i
imap <F1> <esc>:bp<cr>i
" следующий буфер
map <F2> :bn<cr>
vmap <F2> <esc>:bn<cr>i
imap <F2> <esc>:bn<cr>i
" nordetree обозреватель файлов
map <F12> :NERDTreeToggle<cr>
vmap <F12> <esc>:NERDTreeToggle<cr>i
imap <F12> <esc>:NERDTreeToggle<cr>i
" C-e - комментировать/раскомментировать (при помощи NERD_Comment)
map <C-e> \cij
nmap <C-e> \cij
imap <C-e> <ESC>\ciij


" PLUGINS SETTINGS
" vim-showmarks
let g:showmarks_marks="qwertyuiop[]\\asdfghjkl;'zxcbnm,./QWERTYUIOP{}\":LKJHGFDSAZXCVBNM<>?1234567890-=`~!@#$%^&*()_+"
set updatetime=100
autocmd VimEnter * DoShowMarks
let g:indentLine_char = '┆'
" vim-table-mode
autocmd VimEnter * TableModeEnable
let g:table_mode_motion_up_map = '<leader>j'
let g:table_mode_motion_down_map = '<leader>j'
let g:table_mode_motion_left_map = '<leader>h'
let g:table_mode_motion_right_map = '<leader>l'
" nerdtree
let g:nerdtree_tabs_open_on_gui_startup=0  
let g:nerdtree_tabs_open_on_new_tab=0
let g:session_autoload = 'no'
let g:session_autosave = 'no'
" nerdcomment
" Add spaces after comment delimiters by default
let g:NERDSpaceDelims = 1
" Align line-wise comment delimiters flush left instead of following code indentation
let g:NERDDefaultAlign = 'left'
" Allow commenting and inverting empty lines (useful when commenting a region)
let g:NERDCommentEmptyLines = 1
" Enable trimming of trailing whitespace when uncommenting
let g:NERDTrimTrailingWhitespace = 1
" // for C
let g:NERDAltDelims_c = 1
" nerd tree open on start with directory as an argument
autocmd StdinReadPre * let s:std_in=1
autocmd VimEnter * if argc() == 1 && isdirectory(argv()[0]) && !exists("s:std_in") | exe 'NERDTree' argv()[0] | wincmd p | ene | endif
" pydocstring
let g:pydocstring_templates_dir = '~/.vim/pydocstring/with_at/'
nmap <silent> <C-d> <Plug>(pydocstring) :call PydocImproved()<CR>
" Jump to the next or previous line that has the same level or a lower
" level of indentation than the current line.
"
" exclusive (bool): true: Motion is exclusive
" false: Motion is inclusive
" fwd (bool): true: Go to next line
" false: Go to previous line
" lowerlevel (bool): true: Go to line with lower indentation level
" false: Go to line with the same indentation level
" skipblanks (bool): true: Skip blank lines
" false: Don't skip blank lines
function! NextIndent(exclusive, fwd, lowerlevel, skipblanks)
  let line = line('.')
  let column = col('.')
  let lastline = line('$')
  let indent = indent(line)
  let stepvalue = a:fwd ? 1 : -1
  while (line > 0 && line <= lastline)
    let line = line + stepvalue
    if ( ! a:lowerlevel && indent(line) == indent ||
          \ a:lowerlevel && indent(line) < indent)
      if (! a:skipblanks || strlen(getline(line)) > 0)
        if (a:exclusive)
          let line = line - stepvalue
        endif
        exe line
        exe "normal " column . "|"
        return
      endif
    endif
  endwhile
endfunction
" adds return line if there is return statemant in function
function! PydocImproved() 
  normal 2k
  let a:cursor_pos = getpos('.')
  :call NextIndent(1, 1, 0, 1)
  let a:new_pos = getpos('.')
  :call cursor(a:cursor_pos[1], a:cursor_pos[2])
  " let res = execute( "normal :" a:cursor_pos[1] "," a:new_pos[1] "g/return/") | echo res
  :call search("return", "", a:new_pos[1])
  let a:search_pos = getpos('.')
  if a:search_pos[1] != a:cursor_pos[1]
    :call search("\"\"\"")
    :call search("\"\"\"")
    normal O@return: 
    :call search("\"\"\"", 'b')
    normal j$
  endif
endfunction
" syntastic
set statusline+=%#warningmsg#
set statusline+=%{SyntasticStatuslineFlag()}
set statusline+=%*
let g:syntastic_enable_signs = 0
let g:syntastic_always_populate_loc_list = 1
let g:syntastic_auto_loc_list = 0
let g:syntastic_check_on_open = 1
let g:syntastic_check_on_wq = 0
let g:syntastic_python_checkers=['flake8']
